package com.example.shoppingproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Model.PayPalConfig;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import co.omise.android.api.Client;
import co.omise.android.api.Request;
import co.omise.android.api.RequestListener;
import co.omise.android.models.Capability;
import kotlin.text.StringsKt;


public class ConfirmFinalOrderActivity<val> extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confrimOrderBtn;
    private String totalAmount = "";
    private TextView totalPrice, currencyEdit;


    //--------OMIES--------
    private static String PUBLIC_KEY = "pkey_test_5idvjypsdpvqytorxfs";
//    private static String PUBLIC_KEY = "skey_test_5ialne0wqo6lfd82gg0";

    private int AUTHORIZING_PAYMENT_REQUEST_CODE = 0x3D5;
    private int PAYMENT_CREATOR_REQUEST_CODE = 0x3D6;
    private int CREDIT_CARD_REQUEST_CODE = 0x3D7;
    private Snackbar snackbar;
    private Capability capability;
//-----------END-----------

    //    PAYPAL
    private int PAYPAL_REQUEST_CODE = 0x3D5;
//    END


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);

        confrimOrderBtn = (Button) findViewById(R.id.btn_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = " + totalAmount + " ฿", Toast.LENGTH_SHORT).show();

        nameEditText = (EditText) findViewById(R.id.shipment_name);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone);
        addressEditText = (EditText) findViewById(R.id.shipment_address);
        cityEditText = (EditText) findViewById(R.id.shipment_city);

        totalPrice = (TextView) findViewById(R.id.total_price);
        currencyEdit = (TextView) findViewById(R.id.currencyEdit);


        snackbar = Snackbar.make(findViewById(R.id.content), "", Snackbar.LENGTH_SHORT);
        totalPrice.setText(totalAmount);

        DatabaseReference addressRef1 = FirebaseDatabase.getInstance()
                .getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone()).child("address");

        if (addressRef1 != null) {
            showAddress();
        }


        confrimOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "กรุณาระบุชื่อเต็ม", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "กรุณาระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();
                } else if ((phoneEditText.getText().toString()).length() < 10) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "กรุณาระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "กรุณาระบุที่อยู่", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(cityEditText.getText().toString())) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();
                } else if ((cityEditText.getText().toString()).length() < 5) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();
                } else {

                    CharSequence options[] = new CharSequence[]
                            {
                                    "ใช่",
                                    "ไม่"
                            };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmFinalOrderActivity.this);
                    builder.setTitle("ลูกค้าต้องการให้จัดส่งตามที่อยู่ที่ระบุไว้ ?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                payPalPayment();
                                finish();
                            }
                            if (position == 1) {
                                finish();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });


        Client client = new Client(PUBLIC_KEY);
        Request<Capability> request = new Capability.GetCapabilitiesRequestBuilder().build();
        client.send(request, new RequestListener<Capability>() {
            @Override
            public void onRequestSucceed(@NotNull Capability model) {
                capability = model;
            }

            @Override
            public void onRequestFailed(@NotNull Throwable throwable) {
                Toast.makeText(ConfirmFinalOrderActivity.this, "capitalize", Toast.LENGTH_SHORT).show();
                snackbar.setText(StringsKt.capitalize(throwable.getMessage())).show();
            }
        });


    }

    private void showAddress() {

        final DatabaseReference addressRef = FirebaseDatabase.getInstance()
                .getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String aName = dataSnapshot.child("name surname").getValue().toString();
                String aPhone = dataSnapshot.child("phoneOrder").getValue().toString();
                String aAddress = dataSnapshot.child("address").getValue().toString();
                String aPostalCode = dataSnapshot.child("postalCode").getValue().toString();

                nameEditText.setText(aName);
                phoneEditText.setText(aPhone);
                addressEditText.setText(aAddress);
                cityEditText.setText(aPostalCode);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    //----------------------------------------------------omise----------------------------------------------------

//    private void choosePaymentMethod() {
//
////        boolean isUsedSpecificsPaymentMethods = PaymentSetting.isUsedSpecificsPaymentMethods(this);
//
//        if (!isUsedSpecificsPaymentMethods && capability == null) {
//            snackbar.setText(R.string.error_capability_have_not_set_yet);
//            return;
//        }
//
//        double localAmount = Double.valueOf(totalPrice.getText().toString().trim());
//        String currency = currencyEdit.getText().toString().trim().toLowerCase();
//        Amount amount = Amount.fromLocalAmount(localAmount, currency);
//
//        Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentCreatorActivity.class);
//        intent.putExtra(OmiseActivity.EXTRA_PKEY, PUBLIC_KEY);
//        intent.putExtra(OmiseActivity.EXTRA_AMOUNT, amount.getAmount());
//        intent.putExtra(OmiseActivity.EXTRA_CURRENCY, amount.getCurrency());
//
//        if (isUsedSpecificsPaymentMethods) {
//            intent.putExtra(OmiseActivity.EXTRA_CAPABILITY, PaymentSetting.createCapabilityFromPreferences(this));
//        } else {
//            intent.putExtra(OmiseActivity.EXTRA_CAPABILITY, capability);
//        }
//
//        startActivityForResult(intent,PAYMENT_CREATOR_REQUEST_CODE);
//    }

//    private void payByCreditCard() {
//        Intent intent = new Intent(this, CreditCardActivity.class);
//        intent.putExtra(OmiseActivity.EXTRA_PKEY, PUBLIC_KEY);
//        startActivityForResult(intent, CREDIT_CARD_REQUEST_CODE);
//    }
//
//    private void authorizeUrl() {
//        Intent intent = new Intent(this, AuthorizingPaymentActivity.class);
//        intent.putExtra(EXTRA_AUTHORIZED_URLSTRING, "https://pay.omise.co/offsites/");
//        intent.putExtra(EXTRA_EXPECTED_RETURN_URLSTRING_PATTERNS, new String[]{"http://www.example.com"});
//        startActivityForResult(intent, AUTHORIZING_PAYMENT_REQUEST_CODE);
//    }
////
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CANCELED) {
//
//            Toast.makeText(this, "การสร้างการชำระเงินถูกยกเลิก", Toast.LENGTH_SHORT).show();
//            snackbar.setText("การสร้างการชำระเงินถูกยกเลิก").show();
//            return;
//        }

//        if (requestCode == AUTHORIZING_PAYMENT_REQUEST_CODE) {
//            String url = data.getStringExtra(EXTRA_RETURNED_URLSTRING);
////            snackbar.setText(url).show();
//        }
//        else if (requestCode == PAYPAL_REQUEST_CODE) {
//            if (data.hasExtra(OmiseActivity.EXTRA_SOURCE_OBJECT)) {
//                Source source = data.getParcelableExtra(OmiseActivity.EXTRA_SOURCE_OBJECT);
//                Toast.makeText(this, source.getId(), Toast.LENGTH_SHORT).show();
//                snackbar.setText(source.getId()).show();
//            } else if (data.hasExtra(OmiseActivity.EXTRA_TOKEN)) {
//                Token token = data.getParcelableExtra(OmiseActivity.EXTRA_TOKEN_OBJECT);
//                Toast.makeText(this, token.getId(), Toast.LENGTH_SHORT).show();
//                snackbar.setText(token.getId()).show();
//            }
//        }
//        else if (requestCode == CREDIT_CARD_REQUEST_CODE) {
//            Token token = data.getParcelableExtra(OmiseActivity.EXTRA_TOKEN_OBJECT);
//            Toast.makeText(this, token.getId(), Toast.LENGTH_SHORT).show();
//            snackbar.setText(token.getId()).show();
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    //-----------------------------------------END--------omise----------------------------------------------------
    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    private void payPalPayment() {
        PayPalPayment payPalPaymenta = new PayPalPayment(new BigDecimal(totalAmount), "THB", "ProjectShopping",
//        PayPalPayment payPalPaymenta = new PayPalPayment(new BigDecimal(totalAmount), "USD","ProjectShopping",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPaymenta);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//
                if (paymentConfirmation != null) {
                    ComfirmOrder();

                }


            } else {


                Toast.makeText(getApplicationContext(), "การชำระเงินไม่สำเร็จ", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(ConfirmFinalOrderActivity.this, PayPalService.class));
    }


    private void ComfirmOrder() {

        final String OrdersRandomKay;
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        OrdersRandomKay = saveCurrentDate + "," + saveCurrentTime;


        String address = addressEditText.getText().toString() + " " + cityEditText.getText().toString();


        final DatabaseReference Orderlist = FirebaseDatabase.getInstance()
                .getReference().child("Cart List")
                .child(Prevalent.currentOnlineUser.getPhone());


        final DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Orders")
//                .child(Prevalent.currentOnlineUser.getPhone())
                .child(OrdersRandomKay);


        final HashMap<String, Object> orderMap = new HashMap<>();


        orderMap.put("oid", OrdersRandomKay);
        orderMap.put("userName", Prevalent.currentOnlineUser.getPhone());
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("address", address);
//        orderMap.put("city", cityEditText.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
//        orderMap.put("state approve", "not approve");
        orderMap.put("state shipped", "");
        orderMap.put("payment", "ชำระเงินสำเร็จ");
        orderMap.put("package", "");

        Orderlist.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderRef.child("orderList").setValue(dataSnapshot.getValue());

                orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Orderlist.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "คำสั่งซื้อสุดท้ายเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();


                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });

                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}

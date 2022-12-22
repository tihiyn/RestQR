package com.example.restqr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.restqr.Adaptor.CartListAdapter;
import com.example.restqr.Domain.FoodDomain;
import com.example.restqr.Helper.ManagementCart;
import com.example.restqr.Interface.ChangeNumberItemsListener;
import com.example.restqr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CartListActivity extends AppCompatActivity {
private RecyclerView.Adapter adapter;
private RecyclerView recyclerViewList;
private ManagementCart managementCart;
TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
private double tax;
private ScrollView scrollView;
private static String qrString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        managementCart=new ManagementCart(this);

        initView();
        initList();
        CalculateCart();
        bottomNavigation();
        composeString();

        LinearLayout generateBtn = findViewById(R.id.generateBtn);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this, QrCodeActivity.class));
            }
        });
    }

    private void bottomNavigation() {
        FloatingActionButton floatingActionButton=findViewById(R.id.cartBtn);
        LinearLayout homeBtn=findViewById(R.id.homeBtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this, CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this, MainActivity.class));
            }
        });
    }

    private void initView() {
        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        taxTxt=findViewById(R.id.taxTxt);
        deliveryTxt=findViewById(R.id.deliveryTxt);
        totalTxt=findViewById(R.id.totalTxt);
        emptyTxt=findViewById(R.id.emptyTxt);
        scrollView=findViewById(R.id.scrollView3);
        recyclerViewList=findViewById(R.id.cartView);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });
        recyclerViewList.setAdapter(adapter);
        /*if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {*/
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        //}
    }

    private void CalculateCart() {
        double percentTax=0.02;
        double delivery=0;

        tax=Math.round((managementCart.getTotalFee()*percentTax)*100)/100;
        double itemTotal=Math.ceil(managementCart.getTotalFee()*100)/100;
        delivery = Math.round(managementCart.getTotalFee()*10)/100;
        double total=Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;
        totalFeeTxt.setText("$"+itemTotal);
        taxTxt.setText("$"+tax);
        deliveryTxt.setText("$"+delivery);
        totalTxt.setText("$"+total);
    }

    private void composeString() {
        String s = "";
        for (FoodDomain e : managementCart.getListCart()) {
            s += " - " + e.getTitle() + ": x" + e.getNumberInCart() +
                    " - $" + String.format("%.2f", e.getFee() * e.getNumberInCart()) + ";\n";
        }

        double percentTax=0.02;
        double delivery=0;
        tax=Math.round((managementCart.getTotalFee()*percentTax)*100)/100;
        double itemTotal=Math.ceil(managementCart.getTotalFee()*100)/100;
        delivery = Math.round(managementCart.getTotalFee()*10)/100;
        double total=Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;

        s += "Total: $" + Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        qrString = s;
    }

    public static String getQrString() { return qrString; }
}
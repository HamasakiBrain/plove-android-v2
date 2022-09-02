package com.octarine.plove.order;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;


public class OrderStep3Activity extends AppCompatActivity {

    //public RecycleBindCardsAdapter adapter;

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    RelativeLayout rootView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        this.realm = RealmController.with(this).getRealm();
        rootView = (RelativeLayout) findViewById(R.id.content_order_step3);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Пожалуйста, подождите...");
        //progressDialog.setIndeterminate(true);
        //progressDialog.
        loadCards();

*/

    }

    public void initAction() {
        /*
        if (RealmController.getInstance().hasCards()) {
            RealmResults<BindCard> result = RealmController.getInstance().getCards();

            List<BindCardItem> objects = new ArrayList<BindCardItem>();
            objects.add(new BindCardItem(1, "Выберите карту", null, ITEM_TYPE_HEADER, ""));
            int id = 10;
            for (BindCard card: result) {
                Drawable card_image = getResources().getDrawable(R.drawable.noname_card);
                if (card.getCard_type() == 1) card_image = getResources().getDrawable(R.drawable.maestro_card);
                if (card.getCard_type() == 2) card_image = getResources().getDrawable(R.drawable.visa_card);
                objects.add(new BindCardItem(id, card.getPlaceholder(), card_image, ITEM_TYPE_NORMAL, card.getBinding_id()));
                id++;
            }

            objects.add(new BindCardItem(555, "Оплатить новой картой", null, ITEM_TYPE_NORMAL, ""));
            adapter = new RecycleBindCardsAdapter(this, objects);
            recyclerView.setAdapter(adapter);


        } else {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_select_cafe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            //adapter.check();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



}

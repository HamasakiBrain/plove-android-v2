package com.octarine.plove.settings;

/**
 * Created by rustam on 06.02.2018.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.OptionsItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.auth.AuthActivity;
import com.octarine.plove.pay.WebActivity;
import com.octarine.plove.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;


public class RecycleOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OptionsItem> objects = new ArrayList<OptionsItem>();

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;

    private Context context;

    public RecycleOptionsAdapter(Context context, List<OptionsItem> data) {
        this.context = context;
        this.objects = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_NORMAL) {
            View normalView = LayoutInflater.from(context).inflate(R.layout.options_item_row, null);
            return RecyclerOptionsItemViewHolder.newInstance(normalView);
        } else if (viewType == ITEM_TYPE_HEADER) {
            View headerRow = LayoutInflater.from(context).inflate(R.layout.options_header_row, null);
            return RecyclerOptionsHeaderViewHolder.newInstance(headerRow); // view holder for header items
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_NORMAL) {

            RecyclerOptionsItemViewHolder holder = (RecyclerOptionsItemViewHolder) viewholder;
            final OptionsItem item = objects.get(position);
            holder.setTitle(item.title);
            holder.mBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.id == 1) {
                        Intent i = new Intent(context, ProfileActivity.class);
                        context.startActivity(i);
                    } else if (item.id == 2) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Удаление адреса доставки и прочих контактных данных");
                        builder.setMessage("Вы уверены?");

                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.plove_red));
                        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.plove_red));

                    } else if (item.id == 3) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Выход из приложения");
                        builder.setMessage("Вы уверены?");

                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AuthModel authModel = AuthModel.getInstance(context);
                                authModel.sessionId = null;
                                authModel.save(context);
                                ProfileModel pm = ProfileModel.getInstance(context);
                                pm.sessionId = null;
                                pm.save(context);
                                Intent intent = new Intent(context, AuthActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.plove_red));
                        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.plove_red));
                    } else  if (item.id == 4) {
                        Intent i = new Intent(context, WebActivity.class);
                        i.putExtra("url", "http://iloveplove.ru/kontakty/raw.php");
                        i.putExtra("title", "Контакты");
                        context.startActivity(i);
                    } else  if (item.id == 5) {
                        Intent i = new Intent(context, WebActivity.class);
                        i.putExtra("url", "http://iloveplove.ru/rules/raw.php");
                        i.putExtra("title", "Правила оказания услуг");
                        context.startActivity(i);
                    }

                }
            });

        }  else if (itemType == ITEM_TYPE_HEADER) {
            RecyclerOptionsHeaderViewHolder holder = (RecyclerOptionsHeaderViewHolder) viewholder;
            OptionsItem item = objects.get(position);
            holder.setTitle(item.title);
        }
    }



    @Override
    public int getItemViewType(int position) {
        return objects.get(position).objectType;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


}
package com.octarine.plove.reservation;

/**
 * Created by rustam on 06.02.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.order.RecyclerOrderChangerViewHolder;
import com.octarine.plove.order.RecyclerOrderStepHeaderViewHolder;
import com.octarine.plove.order.RecyclerOrderStepItemEditableViewHolder;
import com.octarine.plove.order.RecyclerOrderStepItemSekkBarViewHolder;
import com.octarine.plove.order.RecyclerOrderStepItemViewHolder;
import com.octarine.plove.order.RecyclerOrderStepKeyValueItemViewHolder;
import java.util.ArrayList;
import java.util.List;


public class RecycleStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderStepItem> objects = new ArrayList<>();

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int ITEM_TYPE_EDIT = 2;
    public static final int ITEM_TYPE_CHANGER = 3;
    public static final int ITEM_TYPE_KEY_VALUE = 4;
    public static final int ITEM_TYPE_SEEK = 5;

    private final Context mContext;
    private final ReservationProfileActivity.InteractionListener mListener;

    public RecycleStepAdapter(Context context, List<OrderStepItem> data, ReservationProfileActivity.InteractionListener listener) {
        this.mContext = context;
        this.objects = data;
        this.mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_NORMAL) {
            View normalView = LayoutInflater.from(mContext).inflate(R.layout.order_item_row, null);
            return RecyclerOrderStepItemViewHolder.newInstance(normalView);
        } else if (viewType == ITEM_TYPE_HEADER) {
            View headerRow = LayoutInflater.from(mContext).inflate(R.layout.order_header_row, null);
            return RecyclerOrderStepHeaderViewHolder.newInstance(headerRow); // view holder for header items
        } else if (viewType == ITEM_TYPE_EDIT) {
            View headerRow = LayoutInflater.from(mContext).inflate(R.layout.order_item_editable_row, null);
            return RecyclerOrderStepItemEditableViewHolder.newInstance(headerRow); // view holder for header items
        }
        else if (viewType == ITEM_TYPE_CHANGER) {
            View headerRow = LayoutInflater.from(mContext).inflate(R.layout.order_changer_item_row, null);
            return RecyclerOrderChangerViewHolder.newInstance(headerRow); // view holder for header items
        }
        else if (viewType == ITEM_TYPE_KEY_VALUE) {
            View headerRow = LayoutInflater.from(mContext).inflate(R.layout.order_item_key_value_row, null);
            return RecyclerOrderStepKeyValueItemViewHolder.newInstance(headerRow); // view holder for header items
        }
        else if (viewType == ITEM_TYPE_SEEK) {
            View headerRow = LayoutInflater.from(mContext).inflate(R.layout.order_item_seekbar_row, null);
            return RecyclerOrderStepItemSekkBarViewHolder.newInstance(headerRow); // view holder for header items
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_NORMAL) {

            RecyclerOrderStepItemViewHolder holder = (RecyclerOrderStepItemViewHolder) viewholder;
            OrderStepItem item = objects.get(position);
            holder.setTitle(item.content);

            if (item.checked) {
                holder.setChecked();
            } else {
                holder.setUnChecked();
            }

            holder.mBackground.setOnClickListener(view -> mListener.InteractionTypeSelector(item));

        } else if (itemType == ITEM_TYPE_EDIT) {

            final RecyclerOrderStepItemEditableViewHolder holder = (RecyclerOrderStepItemEditableViewHolder) viewholder;

            OrderStepItem item = objects.get(position);
            holder.setTitle(item.content);
            holder.setUnChecked();
            holder.setValue(item.value);

            holder.mTextViewTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        mListener.InteractionTypeEdit(item, ((EditText) v).getText().toString());
                    }
                }
            });
            /*
            holder.mTextViewTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (holder.mTextViewTitle.getText().toString().length() > 0) {
                        holder.setAlert(false, context);
                        holder.setChecked();
                        MyApplication.RESERVE_CONTACTS[item.systemCode] = holder.mTextViewTitle.getText().toString();
                        item.value = holder.mTextViewTitle.getText().toString();

                    } else {
                        holder.setUnChecked();
                        MyApplication.RESERVE_CONTACTS[item.systemCode] = "";
                        item.value = "";
                        //setAlert(true, context);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });*/

        } else if (itemType == ITEM_TYPE_HEADER) {
            RecyclerOrderStepHeaderViewHolder holder = (RecyclerOrderStepHeaderViewHolder) viewholder;
            OrderStepItem item = objects.get(position);
            holder.setTitle(item.content);

        } else if (itemType == ITEM_TYPE_CHANGER) {
            RecyclerOrderChangerViewHolder holder = (RecyclerOrderChangerViewHolder) viewholder;
            OrderStepItem item = objects.get(position);

            holder.setTitle(item.content);
            holder.setQuantity(item.value);

            holder.mPlus.setOnClickListener(view -> {
                int current_value = Integer.parseInt(item.value);
                current_value++;
                mListener.InteractionTypeChanger(item, String.valueOf(current_value));
            });

            holder.mMinus.setOnClickListener(view -> {
                int current_value = Integer.parseInt(item.value);
                if (current_value > 1)
                    current_value--;
                mListener.InteractionTypeChanger(item, String.valueOf(current_value));
            });

        } else if (itemType == ITEM_TYPE_KEY_VALUE) {

            RecyclerOrderStepKeyValueItemViewHolder holder = (RecyclerOrderStepKeyValueItemViewHolder) viewholder;
            OrderStepItem item = objects.get(position);
            holder.setTitle(item.content);
            holder.setValue(item.value);
            holder.mTextViewValue.setOnClickListener(view -> mListener.InteractionTypeKeyValue(item));


        }
        else if (itemType == ITEM_TYPE_SEEK) {

            final RecyclerOrderStepItemSekkBarViewHolder holder = (RecyclerOrderStepItemSekkBarViewHolder) viewholder;
            OrderStepItem item = objects.get(position);
            holder.setTitle(item.content);
            holder.setValue(item.value);
            holder.setBarMax(item.max);
            holder.mBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b)
                        mListener.InteractionTypeSlider(item, String.valueOf(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

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
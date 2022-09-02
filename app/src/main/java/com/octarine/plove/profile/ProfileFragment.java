package com.octarine.plove.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.BroneModel;
import com.octarine.plove.api.models.NewsResponseModel;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.api.models.ProfileResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.catalog.CategoryActivity;
import com.octarine.plove.common.ObjectSerializer;
import com.octarine.plove.feedback.FeedbackActivity;
import com.octarine.plove.qr.ScannerActivity;
import com.octarine.plove.reservation.ReservationDoneActivity;
import com.octarine.plove.reservation.ReservationSelectCafeActivity;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    Fragment fragment;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public ProfileAdapter mAdapter;
    public List<Object> mItems = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        this.onRefresh();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main_menu, container, false);
        ProfileModel user = ProfileModel.getInstance(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        mItems.clear();
        mItems.add(user.balance);

        mAdapter = new ProfileAdapter(getActivity(), mItems, new OnListFragmentInteractionListener() {
            @Override
            public void onDealClick(NewsResponseModel model) {
                Intent i = new Intent(getActivity(), OpenSaleActivity.class);
                i.putExtra(OpenSaleActivity.EXTRA_CAFE, Parcels.wrap(model));
                startActivity(i);
            }

            @Override
            public void onServiceClick(Service s) {
                if (s.code == 1) {
                    ApplicationController.getInstance().showLoading(getActivity());
                    ApplicationController.getApi().checkStatus(1).enqueue(new Callback<AuthResponseModel>() {
                        @Override
                        public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {

                            ApplicationController.getInstance().hideLoading();

                            if (response.isSuccessful()) {
                                if (response.body().code == 0) {
                                    Intent i = new Intent(getActivity(), CategoryActivity.class);
                                    i.putExtra(CategoryActivity.EXTRA_FROM, CategoryActivity.FROM_DELIVERY);
                                    startActivity(i);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Сообщение");
                                    builder.setMessage(response.body().message);
                                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } else {
                                ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                            ApplicationController.getInstance().hideLoading();
                            ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                        }
                    });

                }
                if (s.code == 2) {
                    ApplicationController.getInstance().showLoading(getActivity());
                    ApplicationController.getApi().checkStatus(2).enqueue(new Callback<AuthResponseModel>() {
                        @Override
                        public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {

                            ApplicationController.getInstance().hideLoading();

                            if (response.isSuccessful()) {
                                if (response.body().code == 0) {
                                    Intent i = new Intent(getActivity(), ReservationSelectCafeActivity.class);
                                    i.putExtra(CategoryActivity.EXTRA_FROM, CategoryActivity.FROM_TAKEAWAY);
                                    startActivity(i);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Сообщение");
                                    builder.setMessage(response.body().message);
                                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } else {
                                ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                            ApplicationController.getInstance().hideLoading();
                            ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                        }
                    });

                }
                if (s.code == 3) {
                    BroneModel brone = new BroneModel();
                    SharedPreferences prefs = getActivity().getSharedPreferences("Application", Context.MODE_PRIVATE);
                    try {
                        brone = (BroneModel) ObjectSerializer.deserialize(prefs.getString("brone", ObjectSerializer.serialize(new BroneModel())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (brone.username == null || brone.username.isEmpty()) {

                        ApplicationController.getInstance().showLoading(getActivity());
                        ApplicationController.getApi().checkStatus(2).enqueue(new Callback<AuthResponseModel>() {
                            @Override
                            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {

                                ApplicationController.getInstance().hideLoading();

                                if (response.isSuccessful()) {
                                    if (response.body().code == 0) {
                                        Intent i = new Intent(getActivity(), ReservationSelectCafeActivity.class);
                                        i.putExtra(CategoryActivity.EXTRA_FROM, CategoryActivity.FROM_RESERVATION);
                                        startActivity(i);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Сообщение");
                                        builder.setMessage(response.body().message);
                                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                                }
                            }

                            @Override
                            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                                ApplicationController.getInstance().hideLoading();
                                ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                            }
                        });

                    } else {
                        Intent i = new Intent(getActivity(), ReservationDoneActivity.class);
                        i.putExtra(ReservationDoneActivity.EXTRA_BRONE, Parcels.wrap(brone));
                        startActivity(i);
                    }
                }
                if (s.code == 4) {
                    if (isStoragePermissionGranted(getActivity(), fragment)) {
                        Intent i = new Intent(getActivity(), ScannerActivity.class);
                        getActivity().startActivityForResult(i, 1);
                    }
                }
                if (s.code == 5) {
                    Intent i = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(i);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

//        this.onRefresh();

//        getProfile();
//        getNews();

        return view;
    }

    public void getProfile() {
        ApplicationController.getApi().profile(AuthModel.getInstance(getActivity()).sessionId).enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                try {
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    response.body().getProfileModel().save(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                try {
                    ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getNews() {
        ApplicationController.getInstance().showLoading(getActivity());
        ApplicationController.getApi().news(AuthModel.getInstance(getActivity()).sessionId).enqueue(new Callback<List<NewsResponseModel>>() {
            @Override
            public void onResponse(Call<List<NewsResponseModel>> call, Response<List<NewsResponseModel>> response) {
                ApplicationController.getInstance().hideLoading();
                try {
                    mItems.clear();
                    ProfileModel user = ProfileModel.getInstance(getActivity());
                    mItems.add(user.balance);
                    mItems.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<NewsResponseModel>> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                try {
                    ApplicationController.showError(getActivity(), getString(R.string.connection_error));
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final static int PERMISSION_REQUEST_CODE = 12;

    public static boolean isStoragePermissionGranted(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    fragment.requestPermissions(
                            new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(getActivity(), ScannerActivity.class);
                getActivity().startActivityForResult(i, 1);
            }
        }
    }

    @Override
    public void onRefresh() {
        getProfile();
        getNews();
        ((MainActivity)(getActivity())).updateBasket();
    }

    public interface OnListFragmentInteractionListener {
        void onDealClick(NewsResponseModel model);
        void onServiceClick(Service s);
    }

}

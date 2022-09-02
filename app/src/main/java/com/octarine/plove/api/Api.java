package com.octarine.plove.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import com.google.gson.JsonObject;
import com.octarine.plove.api.models.*;

public interface Api {

    String PASSWORD = "password";
    String SESSION_ID = "session_id";
    String LOGIN = "login";

    String FIRST_NAME = "firstname";
    String LAST_NAME = "lastname";

    String EMAIL = "email";
    String BIRTHDAY = "birthday";
    String GENDER = "sex";
    String PHONE = "phone";

    String CODE = "code";
    String CARD = "card";
    String CARDNUMBER = "cardnumber";
    String REGION = "region";

    String NOTIFY_SMS = "sms_notify";
    String RANDOM = "random";
    String NOTIFY_EMAIL = "email_notify";
    String ID = "id";

    String LIMIT = "limit";
    String OFFSET = "offset";
    String PARTNER_ID = "partner_id";

    String CURRENT_PHONE = "current_phone";
    String CURRENT_PASSWORD = "current_password";
    String NEW_PASSWORD = "new_password";
    String NEW_PHONE = "new_phone";

    String PARAM = "param";

    String FROM = "from";
    String TO = "to";

    String TYPE = "type";

    String MESSAGE = "message";
    String MESSAGE_TYPE = "message_type";

    String ACTION = "action";
    String CRMID = "crm_id";

    String LAT = "lat";
    String LNG = "lng";

    String ATTAINMENT_ID = "attainment_id";
    String QUANTITY = "quantity";
    String PATRONYMIC = "patronymic";
    String USERNAME = "username";
    String PERSONS = "persons";
    String STATION = "station_id";
    String DATETIMEPARAM = "datetime";
    String DATA = "data";
    String MODE = "mode";
    String INFO = "info";

    @FormUrlEncoded
    @POST("api/auth")
    Call<AuthResponseModel> auth(@Field(LOGIN) String login, @Field(PASSWORD) String password);

    @GET("api/checkStatus")
    Call<AuthResponseModel> checkStatus( @Query(value = MODE, encoded = true) int mode);


    @GET("api/meupdate")
    Call<AuthResponseModel> updateProfile( @Query(value = SESSION_ID, encoded = true) String sessionId,
                                           @Query(value = FIRST_NAME, encoded = true) String firstName,
                                           @Query(value = LAST_NAME, encoded = true) String lastName,
                                           @Query(value = EMAIL, encoded = true) String email,
                                           @Query(value = BIRTHDAY, encoded = true) String birthday,
                                           @Query(value = GENDER, encoded = true) String gender,
                                           @Query(value = NOTIFY_SMS, encoded = true) String smsNotify,
                                           @Query(value = NOTIFY_EMAIL, encoded = true) String emailNotify);


    @GET("api/mecreate")
    Call<AuthResponseModel> createProfile(
            @Query(value = FIRST_NAME, encoded = true) String firstName,
            @Query(value = LAST_NAME, encoded = true)  String lastName,
            @Query(value = EMAIL, encoded = true) String email,
            @Query(value = BIRTHDAY, encoded = true)  String birthday,
            @Query(value = GENDER, encoded = true)  String gender,
            @Query(value = NOTIFY_SMS, encoded = true)  String smsNotify,
            @Query(value = NOTIFY_EMAIL, encoded = true)  String emailNotify,
            @Query(value = PHONE, encoded = true)  String phone);


    @GET("api/cafes")
    Call<List<StationModel>> stations(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @GET("api/news")
    Call<List<NewsResponseModel>> news(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @GET("api/categories")
    Call<List<CatalogModel>> catalog(@Query(value = SESSION_ID, encoded = true) String sessionId, @Query(value = TYPE, encoded = true) String type);

    @GET("api/menu")
    Call<List<MenuModel>> menu(@Query(value = SESSION_ID, encoded = true) String sessionId, @Query(value = TYPE, encoded = true) String type
            , @Query(value = CRMID, encoded = true) String crm_id);


    @POST("api/brone")
    Call<AuthResponseModel> createBrone(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                      @Query(value = USERNAME, encoded = true) String username,
                                        @Query(value = PHONE, encoded = true) String phone,
                                        @Query(value = PERSONS, encoded = true) String persons,
                                        @Query(value = STATION, encoded = true) String station_id,
                                        @Query(value = DATETIMEPARAM, encoded = true) String datetime,
                                        @Query(value = INFO, encoded = true) String info);



    @POST("api/bronecancel")
    Call<AuthResponseModel> cancelBrone(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @POST("api/qr")
    Call<QrResponseModel> scanQr(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                      @Query(value = DATA, encoded = true) String data);
    @POST("api/withoutpayqr")
    Call<AuthResponseModel> withoutPayQr(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                       @Query(value = "visit_id", encoded = true) String visit_id,
                                       @Query(value = "order_id", encoded = true) String order_id,
                                       @Query(value = "station_id", encoded = true) String station_id);

    @POST("api/payqr")
    Call<AuthResponseModel> payQr(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                 @Query(value = "visit_id", encoded = true) String visit_id,
                                @Query(value = "order_id", encoded = true) String order_id,
                                    @Query(value = "station_id", encoded = true) String station_id,
                                        @Query(value = "discount", encoded = true) String discount,
                                        @Query(value = "total", encoded = true) String total,
                                        @Query(value = "pay_type", encoded = true) String pay_type);


    @POST("v1/me/link_card")
    Call<AuthResponseModel> linkCard(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                     @Query(value = CARD, encoded = true) String card);


    @POST("api/meupdatelogin")
    Call<AuthResponseModel> changePhone(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                        @Query(value = CURRENT_PHONE, encoded = true) String phone,
                                        @Query(value = CURRENT_PASSWORD, encoded = true) String currentPassword,
                                        @Query(value = NEW_PHONE, encoded = true) String newPhone);

    @POST("api/meregistration")
    Call<AuthResponseModel> register(@Query(value = PHONE, encoded = true) String phone);

    @POST("api/meregistrationactivate")
    Call<AuthResponseModel> registerActivate(@Query(value = SESSION_ID, encoded = true) String sessionId, @Query(value = CODE, encoded = true) String code);


    @POST("api/melost")
    Call<AuthResponseModel> recovery(@Query(value = PHONE, encoded = true) String phone);

    @POST("api/mesearch")
    Call<AuthResponseModel> mesearch(@Query(value = PHONE, encoded = true) String phone, @Query(value = CARDNUMBER, encoded = true) String card, @Query(value = BIRTHDAY, encoded = true) String bday,
                                     @Query(value = SESSION_ID, encoded = true) String sessionId);

    @GET("api/mehistory")
    Call<HistoryResponseModel> history(
            @Query(value = SESSION_ID, encoded = true) String sessionId,
            @Query(value = LIMIT) int limit,
            @Query(value = OFFSET) int offset,
            @Query(value = RANDOM) String random);

    @GET("api/me")
    Call<ProfileResponseModel> profile(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @GET("v1/attainments_ex_android")
    Call<List<AttainmentExResponseModel>> attainments(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @FormUrlEncoded
    @POST("api/melost")
    Call<AuthResponseModel> sendRecoverSms(@Field(PHONE) String phone);

    @GET("v1/partners_v2")
    Call<List<SalePartnerResponseModel>> partners(@Query(value = SESSION_ID, encoded = true) String sessionId, @Query(value = REGION, encoded = true) String region, @Query(value = LAT, encoded = true) String lat, @Query(value = LNG, encoded = true) String lng);

    @GET("v1/me/statistics")
    Call<List<StatisticModel>> statistic(@Query(value = SESSION_ID, encoded = true) String sessionId, @Query(value = FROM, encoded = true) String from, @Query(value = TO, encoded = true) String to);


    @GET("v1/services")
    Observable<List<ServiceModel>> services(@Query(value = SESSION_ID, encoded = true) String sessionId);

    @POST("v1/me/buy")
    Call<AuthResponseModel> buy(@Query(value = SESSION_ID, encoded = true) String sessionId,
                                @Query(value = ATTAINMENT_ID, encoded = true) String attainment_id,
                                @Query(value = QUANTITY, encoded = true) String quantity);

    @POST("api/order")
    Call<AuthResponseModel> order(@Body JsonObject body, @Query(value = SESSION_ID, encoded = true) String sessionId);
}
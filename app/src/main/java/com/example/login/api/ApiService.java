package com.example.login.api;

import com.example.login.model.Item;
import com.example.login.model.Order;
import com.example.login.model.OrderItem;
import com.example.login.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setLenient().create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://buywithme-aab54-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("user/.json?print=pretty")
    Call<JsonObject> getUserList();
    @GET("user/{id}/.json?print=pretty")
    Call<User> getUser(@Path("id") String id);
    @PUT("user/{id}.json")
    Call<User>signUp(@Path("id") String id,@Body User user);
    @PUT("item/{id}.json")
    Call<Item> editItem(@Path("id") String id, @Body Item item);
    @GET("item/.json?print=pretty")
    Call<JsonObject> getItemList();
    @GET("item/{id}/.json?print=pretty")
    Call<Item> getItem(@Path("id") String id);
    @DELETE("item/{id}.json")
    Call<ResponseBody> deleteItem(@Path("id") String id);
    @PUT("orderitem/{id}.json")
    Call<OrderItem> editOrderItem(@Path("id") String id, @Body OrderItem orderitem);
    @PUT("orderitem/{id}/quantity/.json")
    Call<Long> editOrderItemQuantity(@Path("id") String id, @Body long q);
    @DELETE("orderitem/{id}.json")
    Call<ResponseBody> deleteOrderItem(@Path("id") String id);
    @GET("orderitem/.json?print=pretty")
    Call<JsonObject> getOrderItemList();
    @GET("orderitem/{id}/.json?print=pretty")
    Call<OrderItem> getOrderItem(@Path("id") String id);
    @PUT("order/{id}.json")
    Call<Order> editOrder(@Path("id") String id, @Body Order q);
    @DELETE("order/{id}.json")
    Call<ResponseBody> deleteOrder(@Path("id") String id);
    @GET("order/.json?print=pretty")
    Call<JsonObject> getOrderList();
    @GET("order/{id}/.json?print=pretty")
    Call<Order> getOrder(@Path("id") String id);

}

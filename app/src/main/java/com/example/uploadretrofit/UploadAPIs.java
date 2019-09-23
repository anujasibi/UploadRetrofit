package com.example.uploadretrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {
    @Multipart
    @POST("driver_signup/")
    Call<Result> uploadImage(@Part MultipartBody.Part license,@Part MultipartBody.Part photo,@Part MultipartBody.Part rcbook, @Part("name") RequestBody name,@Part("phone_no") RequestBody phone_no,@Part("email") RequestBody email,@Part("city") RequestBody city,@Part("password") RequestBody password,@Part("vehicle_reg") RequestBody vehicle_reg,@Part("license_no") RequestBody license_no,@Part("rcno") RequestBody rcno,@Part("stockpoint") RequestBody stockpoint);
}

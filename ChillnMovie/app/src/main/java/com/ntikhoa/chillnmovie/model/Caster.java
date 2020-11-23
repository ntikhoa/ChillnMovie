package com.ntikhoa.chillnmovie.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caster {

    @SerializedName("adult")
    @Expose
    private boolean adult;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    // 1 female, 2 male
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("known_for_department")
    @Expose
    private String department;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("cast_id")
    @Expose
    private Integer castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("order")
    @Expose
    private Integer order;

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getCharacter() {
        return character;
    }

    public Integer getGender() {
        return gender;
    }

    public static final DiffUtil.ItemCallback<Caster> CALLBACK = new DiffUtil.ItemCallback<Caster>() {
        @Override
        public boolean areItemsTheSame(@NonNull Caster oldItem, @NonNull Caster newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Caster oldItem, @NonNull Caster newItem) {
            return oldItem.id.equals(newItem.id);
        }
    };
}

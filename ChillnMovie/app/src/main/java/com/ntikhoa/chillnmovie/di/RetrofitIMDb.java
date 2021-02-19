package com.ntikhoa.chillnmovie.di;

import com.google.protobuf.DescriptorProtos;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface RetrofitIMDb {
}

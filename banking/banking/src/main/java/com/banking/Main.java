package com.banking;

import com.banking.account.AccountType;
import com.banking.exception.BussinessException;
import com.banking.goal.Goal;
import jakarta.servlet.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public class Main {

    String email = "a@gmail.com";

    public static void main(String[] args) {
//        LocalDate date1 = LocalDate.of(2023, 01, 01);
//        LocalDate date2 = LocalDate.of(2023, 01, 01);
//        StringBuilder b1 = new StringBuilder("Ram");
//        StringBuilder b2 = new StringBuilder("Ram");
//        System.out.println(date1.equals(date2));
//        System.out.println(b1.equals(b2));
        Long l1 = 10L;
        Long l2 = 10L;
        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDateTime now1 = LocalDateTime.now().plusDays(30);
        System.out.println(now1);
        String hello = "Hello";
        byte[] bytes = hello.getBytes();
        System.out.println("Bytes - " + hello.getBytes().length);
//        LocalDate of = LocalDate.ofInstant(Instant.now());
//        System.out.println(of);
//        System.out.println(of);
//        System.out.println(l1 == l2);
//        System.out.println(l1.equals(l2));

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        AccountType[] values = AccountType.values();
//        Optional<AccountType> optional = List.of(values).stream().filter(accountType -> accountType.equals("GGGG")).findFirst();

//        int mYear = calendar.get(Calendar.YEAR);
//        int mMonth = calendar.get(Calendar.MONTH);
//        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        System.out.println("Y -" + mYear + " M - " + mMonth + " D - " + mDay);
//        System.out.println(LocalDate.now().getMonth().getValue());
//        System.out.println(LocalDateTime.now());
//        Double d = 10.32;
//        Double d = 10.87;
//        double r = d - Math.floor(d);
//        BigDecimal bd = new BigDecimal(Double.toString(r));
//        bd = bd.setScale(2, RoundingMode.HALF_DOWN);
//        double f = bd.doubleValue();
//        System.out.println(f);
//        if (f < 0.5) {
//            double d1 = Math.floor(d) + 0.5;
//            System.out.println(d1);
//            BigDecimal bd1 = new BigDecimal(Double.toString(d1-d));
//            bd1 = bd1.setScale(2, RoundingMode.HALF_DOWN);
//            double f2 = bd1.doubleValue();
//            System.out.println(f2);
//        } else {
//            double ceil = Math.ceil(d) - d;
//            BigDecimal bd1 = new BigDecimal(Double.toString(ceil));
//            bd1 = bd1.setScale(2, RoundingMode.HALF_UP);
//            double f2 = bd1.doubleValue();
//            System.out.println(f2);
//            System.out.println(Math.ceil(d));
//        }

//        BigDecimal b = new BigDecimal(Double.toString(1.2434));
//        b.setScale(2, RoundingMode.HALF_DOWN);
//        System.out.println(b.doubleValue());
//        b.setScale(2, RoundingMode.HALF_DOWN);
//        System.out.println(b.doubleValue());
//        b.setScale(2, RoundingMode.DOWN);
//        System.out.println(b.doubleValue());
//        b.setScale(2, RoundingMode.UP);
//        System.out.println(b.doubleValue());

//        System.out.println(Locale.ITALY);

//        System.out.println(System.currentTimeMillis());
//        long timeMillis = System.currentTimeMillis();
//        LocalDateTime d = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.of("Asia/Qatar"));
//        System.out.println(d);
//
//        System.out.println(ZoneId.of("America/New_York"));
//
//        Field[] fields = Main.class.getDeclaredFields();
//        Arrays.asList(fields).stream().forEach(field -> {
//            MyField myField = field.getAnnotation(MyField.class);
//            System.out.println(myField.name());
//            System.out.println(myField.col());
//            try {
//                System.out.println(field.get(new Main()));
//            }  catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(field.getName());
//        } );

//        System.out.println(LocalDateTime.now().getDayOfWeek());

    }

}
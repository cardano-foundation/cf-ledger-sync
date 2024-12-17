// package org.cardanofoundation.ledgersync.scheduler.util;

// import java.text.DecimalFormat;
// import java.text.SimpleDateFormat;
// import java.time.Duration;
// import java.time.Instant;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.Collection;
// import java.util.Date;
// import java.util.Map;

// public class DataUtil {

//   public static boolean isNullOrEmpty(CharSequence cs) {
//     int strLen;
//     if (cs == null || (strLen = cs.length()) == 0) {
//       return true;
//     }
//     for (int i = 0; i < strLen; i++) {
//       if (!Character.isWhitespace(cs.charAt(i))) {
//         return false;
//       }
//     }
//     return true;
//   }

//   public static boolean isNullOrEmpty(final Collection<?> collection) {
//     return collection == null || collection.isEmpty();
//   }

//   public static boolean isNullOrEmpty(final Object obj) {
//     return obj == null || obj.toString().isEmpty();
//   }

//   public static boolean isNullOrEmpty(final Object[] collection) {
//     return collection == null || collection.length == 0;
//   }

//   public static boolean isNullOrEmpty(final Map<?, ?> map) {
//     return map == null || map.isEmpty();
//   }

//   public static String instantToString(Instant value, String pattern) {
//     if (pattern != null) {
//       DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
//       return dtf.format(value.plus(Duration.ofHours(7)));
//     }
//     return "";
//   }

//   public static String localDateTimeToString(LocalDateTime value, String pattern) {
//     if (pattern != null) {
//       DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
//       return dtf.format(value.plus(Duration.ofHours(7)));
//     }
//     return "";
//   }

//   public static String dateToString(Date value, String pattern) {
//     if (pattern != null) {
//       SimpleDateFormat dtf = new SimpleDateFormat(pattern);
//       return dtf.format(Date.from(value.toInstant().plus(Duration.ofHours(7))));
//     }
//     return "";
//   }

//   public static String enumToString(Enum<?> value) {
//     if (value == null) {
//       return "";
//     }
//     String text = value.name();
//     text = text.replaceAll("_", " ");
//     text = text.toLowerCase();
//     return text.substring(0, 1).toUpperCase() + text.substring(1);
//   }

//   public static String objectToString(Object value) {
//     return (value == null) ? "" : value.toString();
//   }

//   public static String doubleToString(Double value) {
//     if (value == null) {
//       return "";
//     }
//     DecimalFormat doubleFormat = new DecimalFormat("#.######");
//     String result = doubleFormat.format(value);
//     if (result.endsWith(".0")) {
//       result = result.split("\\.")[0];
//     }
//     return result;
//   }

//   public static String makeLikeQuery(String s) {
//     if (DataUtil.isNullOrEmpty(s)) {
//       return null;
//     }
//     return "%" + s + "%";
//   }
// }

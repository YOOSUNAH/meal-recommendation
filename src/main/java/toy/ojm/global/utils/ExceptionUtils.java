//package toy.ojm.global.utils;
//
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import java.util.function.Consumer;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class ExceptionUtils {
//
//    public static <E extends Exception> void handleException(
//        Runnable successAction,
//        Consumer<E> failAction
//    ) {
//        try {
//            successAction.run();
//        } catch (Exception e) {
//            try {
//                @SuppressWarnings("unchecked")
//                E ex = (E) e;
//                failAction.accept(ex);
//            } catch (ClassCastException ignored) {
//            }
//        }
//    }
//
//
//}

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

    public static boolean not(String val) throws Exception {
        if (val.equals("1")) {
            return false;
        } else if (val.equals("0")) {
            return true;
        } else {
            throw new Exception("неизвестное высказывание");
        }
    }

    public static boolean and(String a, String b) {
        return a.equals("1") && b.equals("1");
    }

    public static boolean or(String a, String b) {
        return a.equals("1") || b.equals("1");
    }

    public static boolean impl(String a, String b) {
        return !(a.equals("1") && b.equals("0"));
    }

    public static boolean equi(String a, String b) {
        return a.equals(b);
    }

    public static String boolToString(boolean val) {
        return val ? "1" : "0";
    }

    public static boolean strToBool(String val) throws Exception {
        if (val.equals("1")) {
            return true;
        } else if (val.equals("0")) {
            return false;
        } else {
            throw new Exception("нет данных");
        }
    }

    public static String applyOperator(String a, String b, String operator) {
        switch (operator) {
            case "And":
                return boolToString(and(a, b));
            case "Or":
                return boolToString(or(a, b));
            case "Impl":
                return boolToString(impl(a, b));
            case "Equi":
                return boolToString(equi(a, b));
            default:
                return "неизвестный оператор";
        }
    }

    public static String logicOper(String comment) {
        String[] tokens = comment.split(" ");
        List<String> values = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    String operator = stack.pop();
                    if (values.size() < 2) {
                        return "неправильное выражение"; // Ошибка при недостатке значений
                    }
                    String valB = values.remove(values.size() - 1);
                    String valA = values.remove(values.size() - 1);
                    String result = applyOperator(valA, valB, operator);
                    values.add(result); // Добавляем результат обратно
                }
                if (stack.isEmpty()) {
                    return "неправильное выражение"; // Закрывающая скобка без открывающей
                }
                stack.pop(); // Удаляем '('
            } else if (token.equals("not")) {
                // Обработка операции NOT
                if (!values.isEmpty()) {
                    String lastValue = values.remove(values.size() - 1);
                    try {
                        boolean result = not(lastValue);
                        values.add(boolToString(result)); // Заменяем на результат
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            } else if (token.equals("And") || token.equals("Or") || token.equals("Impl") || token.equals("Equi")) {
                // Обработка операторов
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    String operator = stack.pop();
                    if (values.size() < 2) {
                        return "неправильное выражение"; // Ошибка при недостатке значений
                    }
                    String valB = values.remove(values.size() - 1);
                    String valA = values.remove(values.size() - 1);
                    String result = applyOperator(valA, valB, operator);
                    values.add(result); // Добавляем результат обратно
                }
                stack.push(token); // Добавляем оператор в стек
            } else if (token.equals("1") || token.equals("0")) {
                // Добавляем значения в массив
                values.add(token);
            }
        }

        while (!stack.isEmpty()) {
            String operator = stack.pop();
            if (values.size() < 2) {
                return "неправильное выражение"; // Ошибка при недостатке значений
            }
            String valB = values.remove(values.size() - 1);
            String valA = values.remove(values.size() - 1);
            String result = applyOperator(valA, valB, operator);
            values.add(result); // Добавляем результат обратно
        }

        if (!values.isEmpty()) {
            return values.get(0); // Возврат результата
        }
        return "нет значений";
    }

    public static int precedence(String op) {
        switch (op) {
            case "not":
                return 3;
            case "And":
                return 2;
            case "Or":
                return 1;
            case "Impl":
            case "Equi":
                return 0;
        }
        return -1;
    }

    public static void main(String[] args) {
        String comment = "0 And 0 Or ( 0 Or 0 ) And ( 1 And not 0 )";
        try {
            System.out.println(strToBool(logicOper(comment)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
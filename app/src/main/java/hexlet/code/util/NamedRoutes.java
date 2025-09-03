package hexlet.code.util;

public class NamedRoutes {

    public static String mainPagePath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String urlChecksPath(Long id) {
        return urlPath(String.valueOf(id)) + "/checks";
    }

    public static String urlChecksPath(String id) {
        return "/urls/" + id + "/checks";
    }
}

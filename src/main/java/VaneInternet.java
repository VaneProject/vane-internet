import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public final class VaneInternet implements Closeable {
    private final HttpURLConnection connection;

    public VaneInternet(@NotNull URL url) throws IOException {
        this.connection = (HttpURLConnection) url.openConnection();
    }

    public VaneInternet(@NotNull String url) throws IOException {
        this(new URL(url));
    }

    public int code() throws IOException {
        connection.connect();
        return connection.getResponseCode();
    }

    public boolean isSuccessful() throws IOException {
        connection.connect();
        int code = connection.getResponseCode();
        return code >= 200 && code < 300;
    }

    public String getRead() throws IOException {
        StringBuilder sb = new StringBuilder();
        connection.connect();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public VaneInternet setRequestMethod(VaneMethod method) throws ProtocolException {
        return setRequestMethod(method.name());
    }

    public VaneInternet setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
        return this;
    }


    public VaneInternet addRequestProperty(String key, String value) {
        connection.setRequestProperty(key, value);
        return this;
    }

    // Authorization
    public VaneInternet addHeader(String value) {
        connection.setRequestProperty("Authorization", value);
        return this;
    }

    // Content-Type
    public VaneInternet addType(String type) {
        connection.addRequestProperty("Content-Type", type);
        return this;
    }

    // Content-Length
    public VaneInternet addLength(int length) {
        connection.setRequestProperty("Content-Length", Integer.toString(length));
        return this;
    }

    // Content-Language
    public VaneInternet addLanguage(String language) {
        connection.setRequestProperty("Content-Language", language);
        return this;
    }

    @Override
    public void close() {
        connection.disconnect();
    }
}

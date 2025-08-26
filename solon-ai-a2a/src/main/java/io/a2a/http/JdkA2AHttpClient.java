package io.a2a.http;

import org.noear.solon.annotation.Http;
import org.noear.solon.net.http.HttpResponse;
import org.noear.solon.net.http.HttpUtils;
import org.noear.solon.net.http.HttpUtilsBuilder;
import org.noear.solon.net.http.textstream.ServerSentEvent;
import org.noear.solon.rx.SimpleSubscriber;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class JdkA2AHttpClient implements A2AHttpClient {
    @Override
    public GetBuilder createGet() {
        return new JdkGetBuilder();
    }

    @Override
    public PostBuilder createPost() {
        return new JdkPostBuilder();
    }

    private abstract class JdkBuilder<T extends Builder<T>> implements Builder<T> {
        protected String url;
        protected Map<String, String> headers = new HashMap<>();

        @Override
        public T url(String url) {
            this.url = url;
            return self();
        }

        @Override
        public T addHeader(String name, String value) {
            headers.put(name, value);
            return self();
        }

        @SuppressWarnings("unchecked")
        T self() {
            return (T) this;
        }

        protected CompletableFuture<Void> asyncSSE(
                HttpUtils request,
                String method,
                Consumer<String> messageConsumer,
                Consumer<Throwable> errorConsumer,
                Runnable completeRunnable) {

            CompletableFuture<Void> future = new CompletableFuture<>();
            request.execAsSseStream(method)
                    .subscribe(new SimpleSubscriber<ServerSentEvent>()
                            .doOnNext(sse -> {
                                messageConsumer.accept(sse.getData());
                            })
                            .doOnError(err -> {
                                try {
                                    errorConsumer.accept(err);
                                } finally {
                                    future.completeExceptionally(err);
                                }
                            })
                            .doOnComplete(() -> {
                                try {
                                    completeRunnable.run();
                                } finally {
                                    future.complete(null);
                                }
                            }));

            return future;
        }
    }

    private class JdkGetBuilder extends JdkBuilder<GetBuilder> implements GetBuilder {

        protected HttpUtils createRequestBuilder(boolean SSE) throws IOException {
            HttpUtils http = HttpUtils.http(url)
                    .charset("utf-8")
                    .headers(headers);

            if (SSE) {
                http.header("Accept", "text/event-stream");
            }

            return http;
        }

        @Override
        public A2AHttpResponse get() throws IOException, InterruptedException {
            HttpUtils request = createRequestBuilder(false);
            HttpResponse response = request.exec("GET");
            return new JdkHttpResponse(response);
        }

        @Override
        public CompletableFuture<Void> getAsyncSSE(
                Consumer<String> messageConsumer,
                Consumer<Throwable> errorConsumer,
                Runnable completeRunnable) throws IOException, InterruptedException {
            HttpUtils request = createRequestBuilder(true);

            return asyncSSE(request, "GET", messageConsumer, errorConsumer, completeRunnable);
        }
    }

    private class JdkPostBuilder extends JdkBuilder<PostBuilder> implements PostBuilder {
        String body = "";

        @Override
        public PostBuilder body(String body) {
            this.body = body;
            return self();
        }

        private HttpUtils createRequestBuilder(boolean SSE) throws IOException {
            HttpUtils http = HttpUtils.http(url)
                    .charset("utf-8")
                    .headers(headers)
                    .body(body.getBytes(StandardCharsets.UTF_8));

            if (SSE) {
                http.header("Accept", "text/event-stream");
            }

            return http;
        }

        @Override
        public A2AHttpResponse post() throws IOException, InterruptedException {
            HttpUtils request = createRequestBuilder(false);
            HttpResponse response = request.exec("POST");
            return new JdkHttpResponse(response);
        }

        @Override
        public CompletableFuture<Void> postAsyncSSE(
                Consumer<String> messageConsumer,
                Consumer<Throwable> errorConsumer,
                Runnable completeRunnable) throws IOException, InterruptedException {
            HttpUtils request = createRequestBuilder(true);

            return asyncSSE(request, "POST", messageConsumer, errorConsumer, completeRunnable);
        }
    }

    private static class JdkHttpResponse implements A2AHttpResponse {
        HttpResponse response;

        public JdkHttpResponse(HttpResponse response) {
            this.response = response;
        }

        @Override
        public int status() {
            return response.code();
        }

        @Override
        public boolean success() {// Send the request and get the response
            return success(response);
        }

        static boolean success(HttpResponse response) {
            return response.code() >= 200 && response.code() < 300;
        }

        @Override
        public String body() {
            try {
                return response.bodyAsString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

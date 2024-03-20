/*
 * Copyright 2020 me.yangbajing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yangbajing.nacos4s.play.ws.javadsl;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.pekko.stream.javadsl.Source;
import org.apache.pekko.util.ByteString;
import org.w3c.dom.Document;
import play.libs.ws.*;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AhcWSResponse implements WSResponse {
    private static final WSBodyReadables readables = new WSBodyReadables() {
    };

    private final StandaloneWSResponse underlying;

    AhcWSResponse(StandaloneWSResponse response) {
        this.underlying = response;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return underlying.getHeaders();
    }

    @Override
    public List<String> getHeaderValues(String name) {
        return underlying.getHeaderValues(name);
    }

    @Override
    public Optional<String> getSingleHeader(String name) {
        return underlying.getSingleHeader(name);
    }

    @Override
    public Object getUnderlying() {
        return underlying.getUnderlying();
    }

    @Override
    public String getContentType() {
        return underlying.getContentType();
    }

    @Override
    public int getStatus() {
        return underlying.getStatus();
    }

    @Override
    public String getStatusText() {
        return underlying.getStatusText();
    }

    @Override
    public List<WSCookie> getCookies() {
        return underlying.getCookies();
    }

    @Override
    public Optional<WSCookie> getCookie(String name) {
        return underlying.getCookie(name);
    }

    @Override
    public ByteString getBodyAsBytes() {
        return underlying.getBodyAsBytes();
    }

    @Override
    public <T> T getBody(BodyReadable<T> readable) {
        return readable.apply(this);
    }

    @Override
    public Source<ByteString, ?> getBodyAsSource() {
        return underlying.getBodyAsSource();
    }

    @Override
    public String getBody() {
        return underlying.getBody();
    }

    @Override
    public URI getUri() {
        return underlying.getUri();
    }

    /**
     * @return the headers
     * @deprecated Deprecated since 2.6.0. Use {@link #getHeaders()} instead.
     */
    @Override
    @Deprecated
    public Map<String, List<String>> getAllHeaders() {
        return underlying.getHeaders();
    }

    /**
     * @deprecated Use {@code response.getBody(xml())}
     */
    @Override
    @Deprecated
    public Document asXml() {
        return underlying.getBody(readables.xml());
    }

    /**
     * @deprecated Use {@code response.getBody(json())}
     */
    @Override
    @Deprecated
    public JsonNode asJson() {
        return underlying.getBody(readables.json());
    }

    /**
     * @deprecated Use {@code response.getBody(inputStream())}
     */
    @Override
    @Deprecated
    public InputStream getBodyAsStream() {
        return underlying.getBody(readables.inputStream());
    }

    /**
     * @deprecated Use {@code response.getBodyAsBytes().toArray()}
     */
    @Override
    @Deprecated
    public byte[] asByteArray() {
        return underlying.getBodyAsBytes().toArray();
    }
}

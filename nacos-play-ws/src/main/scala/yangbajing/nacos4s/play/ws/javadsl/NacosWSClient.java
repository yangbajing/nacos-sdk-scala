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

import akka.actor.ActorSystem;
import akka.actor.typed.javadsl.Adapter;
import akka.stream.SystemMaterializer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.ahc.StandaloneAhcWSClient;
import play.libs.ws.ahc.StandaloneAhcWSRequest;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import yangbajing.nacos4s.client.naming.Nacos4sNamingService;
import yangbajing.nacos4s.client.util.Constants;
import yangbajing.nacos4s.client.util.Nacos4s;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NacosWSClient implements WSClient {
    private final StandaloneAhcWSClient client;
    private final ActorSystem system;
    private final Nacos4sNamingService namingService;

    public NacosWSClient(ActorSystem system, StandaloneAhcWSClient client) {
        this.client = client;
        this.system = system;
        namingService = Nacos4s.namingService(system.settings().config().getConfig(Constants.NACOS4S_CLIENT_NAMING()));
    }

    public NacosWSClient(akka.actor.typed.ActorSystem<?> system, StandaloneAhcWSClient client) {
        this(Adapter.toClassic(system), client);
    }

    @Override
    public Object getUnderlying() {
        return client.getUnderlying();
    }

    @Override
    public play.api.libs.ws.WSClient asScala() {
        return new yangbajing.nacos4s.play.ws.scaladsl.NacosWSClient(system, new play.api.libs.ws.ahc.StandaloneAhcWSClient(
                (AsyncHttpClient) client.getUnderlying(), SystemMaterializer.get(system).materializer()));
    }

    @Override
    public WSRequest url(String url) {
        String realUrl = url;

        try {
            URI uri = new URI(url);
            if (uri.getPort() == -1) {
                Instance inst = namingService.selectOneHealthyInstance(uri.getHost());
                realUrl = new URI(
                        uri.getScheme(),
                        uri.getUserInfo(),
                        inst.getIp(),
                        inst.getPort(),
                        uri.getPath(),
                        uri.getQuery(),
                        uri.getFragment()).toString();
            }
        } catch (@SuppressWarnings("unused") URISyntaxException e) {
            system.log().warning("URL invalid, url is {}.", url);
        } catch (Exception e) {
            system.log().warning("Service discovery failed, direct access. url is {}.", url, e);
        }
        final StandaloneAhcWSRequest plainWSRequest = client.url(realUrl);
        return new AhcWSRequest(this, plainWSRequest);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}

package guru.springframework.spring_6_webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.spring_6_webclient.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@Service
public class BeerClientImpl implements BeerClient {


    private final WebClient webClient;
    private final String BEER_PATH = "/api/v3/beer";
    private final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    public BeerClientImpl(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.build();
    }


    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDtos() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }
}

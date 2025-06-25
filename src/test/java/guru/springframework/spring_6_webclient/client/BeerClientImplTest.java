package guru.springframework.spring_6_webclient.client;

import guru.springframework.spring_6_webclient.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient client;


    @Test
    void listBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeer().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }



    @Test
    void testGetMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }


    @Test
    void testGetBeerJson() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerJsonNode().subscribe(jsonNode -> {
            System.out.println(jsonNode.toPrettyString());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerDto() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos().subscribe(beerDto -> {
            System.out.println(beerDto.getBeerName());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }


    @Test
    void testGetBeerById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                .flatMap(dto -> client.getBeerById(dto.getId()))
                .subscribe(beerDto -> {
                        System.out.println(beerDto.getBeerName());
                        atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerByBeerStyle() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        //this will fail if the string is not contained in any beer, so watch out
        client.getBeerByBeerStyle("PALE_ALE")
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }


    @Test
    void testCreateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .beerName("Cantillon")
                .beerStyle("Sour")
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        //this will fail if the string is not contained in any beer, so watch out
        client.createBeer(newDto)
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }


    @Test
    void testUpdateBeer() {

        final String NAME = "TEST";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                        .next()
                                .doOnNext(beerDTO -> beerDTO.setBeerName(NAME))
                                .flatMap(dto -> client.updateBeer(dto))
                                        .subscribe(byIdDto -> {
                                            System.out.println(byIdDto.toString());
                                            atomicBoolean.set(true);
                                        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testPatchBeer() {

        final String NAME = "TEST";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                .next()
                .map(beerDTO -> BeerDTO.builder().beerName(NAME).beerStyle("IPA").id(beerDTO.getId()).build())
                .flatMap(dto -> client.patchBeer(dto))
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    
    @Test
    void testDeleteBeer() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);


        client.listBeerDtos()
                .next()
                .flatMap(dto -> client.deleteBeer(dto.getId()))
                .doOnSuccess(success -> atomicBoolean.set(true))
                .subscribe();

        await().untilTrue(atomicBoolean);
    }
}
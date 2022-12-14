package com.sotalvaroo.springreactorlearning;

import com.sotalvaroo.springreactorlearning.models.Comment;
import com.sotalvaroo.springreactorlearning.models.User;
import com.sotalvaroo.springreactorlearning.models.UserComment;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SpringReactorLearningApplication implements CommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(SpringReactorLearningApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorLearningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /* Haciendo uso de just


        Flux<User> students = Flux.just("Santiago Otalvaro", "Carlos Daniel", "Camilo Rojas", "Santiago Lopez")
                .map(names -> new User(names.split(" ")[0].toUpperCase(), names.split(" ")[1].toUpperCase()))
                .doOnNext(user -> {
                    if (user == null) {
                        throw new RuntimeException("Nombre no puede ser vacio");
                    }

                })
                .map(user ->{
                    String name = user.getName().toUpperCase();
                    user.setName(name);
                    return user;
                })
                .filter(user->user.getName().equals("SANTIAGO"));

        students.subscribe(e -> {
                    log.info(e.toString());
                },
                err -> {
                    log.error(err.getMessage());
                },
                //OnComplete con Runnable
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable");
                    }
                });


          Haciendo uso de just */

        /*List<String> usersList = Arrays.asList("Santiago Otalvaro", "Carlos Daniel", "Camilo Rojas", "Santiago Lopez");

        Flux<String> names = Flux.fromIterable(usersList)
                .map(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("No puede estar vacio");
                    }
                    return e.toUpperCase();
                });

        names.subscribe(e -> {
                    log.info(e);
                },
                err -> {
                    log.error(err.getMessage());
                },
                //OnComplete con Runnable
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable");
                    }
                });


        Flux.fromIterable(usersList)
                .map(name -> new User(name.split(" ")[0], name.split(" ")[1]))
                .flatMap(user -> {
                    if (user.getName().equalsIgnoreCase("santiago")) {
                        return Mono.just(user);
                    } else {
                        return Mono.empty();
                    }
                })
                .subscribe(u -> log.info(u.toString()));

        Flux.fromIterable(usersList)
                .collectList()
                .subscribe(e->{
                    e.forEach(item ->log.info(item));

                });*/

        /*ejemploUsuarioComentariosFlatMap();*/
        /*ejemploUsuarioComentariosZipWith();*/
        /*ejemploUsuarioComentariosZipWithForma2();*/
        /*ejemploZipWithRange();*/
        /*ejemploInterval();*/
        /*ejemploDelayElements();*/
        /*ejemploIntervaloInfinito();*/
        /*ejemploIntervalDesdeCreate();*/
        ejemploContrapresion();

    }

    public void ejemploUsuarioComentariosFlatMap() {

        Mono<User> userMono = Mono.fromCallable(() -> new User("Santiago", "Otalvaro"));
        Mono<Comment> commentsMono = Mono.fromCallable(() -> {
            Comment comments = new Comment();
            comments.addComments("Hola primer comment");
            comments.addComments("Mundo segundo comment");
            comments.addComments("tercer comment");
            return comments;
        });

        userMono.flatMap(u -> {
            Mono<UserComment> map = commentsMono.map(c -> {
                UserComment userComment = new UserComment(u, c);
                return userComment;
            });
            return map;
        }).subscribe(uc -> log.info(uc.toString()));

    }

    public void ejemploUsuarioComentariosZipWith() {

        Mono<User> userMono = Mono.fromCallable(() -> new User("Santiago", "Otalvaro"));
        Mono<Comment> commentsMono = Mono.fromCallable(() -> {
            Comment comments = new Comment();
            comments.addComments("Hola primer comment");
            comments.addComments("Mundo segundo comment");
            comments.addComments("tercer comment");
            return comments;
        });

        Mono<UserComment> userCommentMono = userMono.zipWith(commentsMono, (u, c) -> new UserComment(u, c));

        userCommentMono.subscribe(uc -> log.info(uc.toString()));

    }

    public void ejemploUsuarioComentariosZipWithForma2() {

        Mono<User> userMono = Mono.fromCallable(() -> new User("Santiago", "Otalvaro"));
        Mono<Comment> commentsMono = Mono.fromCallable(() -> {
            Comment comments = new Comment();
            comments.addComments("Hola primer comment");
            comments.addComments("Mundo segundo comment");
            comments.addComments("tercer comment");
            return comments;
        });

        Mono<UserComment> userCommentMono = userMono
                .zipWith(commentsMono)
                .map(tuple -> {
                    User u = tuple.getT1();
                    Comment c = tuple.getT2();
                    return new UserComment(u, c);
                });

        userCommentMono.subscribe(uc -> log.info(uc.toString()));


    }

    public void ejemploZipWithRange() {
        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .zipWith(Flux.range(0, 4), (uno, dos) -> String.format("Primer flux %s segundo Flux %s", uno, dos))
                .subscribe(e -> log.info(e));
    }

    public void ejemploInterval() {
        Flux<Integer> rango = Flux.range(1, 12);
        Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));

        rango.zipWith(retraso, (r, d) -> r)
                .doOnNext(i -> log.info(i.toString()))
                .blockLast();//solo para ver el ejemplo, pero no es ideal usar block
    }

    public void ejemploDelayElements() throws InterruptedException {
        Flux<Integer> rango = Flux.range(1, 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> log.info(i.toString()));

        /*
        rango.blockLast();//solo para ver el ejemplo, pero no es ideal usar block
        */

        rango.subscribe();
        Thread.sleep(13000);
    }

    public void ejemploIntervaloInfinito() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        Flux.interval(Duration.ofSeconds(1))
                .doOnTerminate(latch::countDown)
                .flatMap(i -> {
                    if (i >= 5) {
                        return Flux.error(new InterruptedException("Solo hasta 5"));
                    }
                    return Flux.just(i);
                })
                .map(i -> "Hello " + i)
                .retry(2)
                .subscribe(s -> log.info(s), e -> log.error(e.getMessage()));

        latch.await();
    }

    public void ejemploIntervalDesdeCreate() {
        Flux.create(emitter -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        private Integer counter = 0;

                        @Override
                        public void run() {
                            emitter.next(++counter);
                            if (counter == 10) {
                                timer.cancel();
                                emitter.complete();
                            }
                            if (counter == 5) {
                                timer.cancel();
                                emitter.error(new InterruptedException("Error, se ha detenido"));
                            }
                        }
                    }, 1000, 1000);
                })
                .subscribe(next -> log.info(next.toString())
                        , err -> log.error(err.getMessage())
                        , () -> log.info("Terminado"));
    }

    public void ejemploContrapresion() {
        Flux.range(1, 10)
                .log()
                .limitRate(5)          //Opcion r??pida
                .subscribe(/*new Subscriber<Integer>() {

                    private Subscription s;

                    private Integer limit = 2;
                    private Integer consumed = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.s = subscription;
                        s.request(limit);

                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info(integer.toString());
                        consumed++;
                        if (consumed == limit){
                            consumed = 0;
                            s.request(limit);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }*/);   //Opcion lenta
    }

}

package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Normalmente o service serve dessas tres maneiras:
// Faço uma lógica e retorno para quem esta chamando
// Pego os dados e repasso
// Pego os dados de uma api de terceiros..
public class ProducerService {
    // Preciso de um repository
    private final ProducerHardCodedRepository repository;

    // Todas as vezes que eu criar um ProducerService automaticamente vou criar um repository
    public ProducerService() {
        this.repository = new ProducerHardCodedRepository();
    }

    // Uma lógica de negocio, se o name for null eu retorno tudo, se nao, eu retorno a busca do nome em especifico
    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    // Retorno o id com base no repository do metodo, porem com a regra de se nao achar retornar uma exception
    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not Found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    // Acho o producer com base na busca pelo id pelo metodo acima
    // Deleto ele com o metodo que criei no meu repository
    public void deleteById(Long id) {
        Producer byIdOrThrowNotFound = findByIdOrThrowNotFound(id);
        repository.deleteById(byIdOrThrowNotFound.getId());
    }

    // Acho o producer que desejo atualizar
    // Atualizo com o metodo criado no repository
    public void update(Producer producer) {
        Producer producerToUpdated = findByIdOrThrowNotFound(producer.getId());
        producer.setCreatedAt(producerToUpdated.getCreatedAt());
        repository.update(producer);
    }

}

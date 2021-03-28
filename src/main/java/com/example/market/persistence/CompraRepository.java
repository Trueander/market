package com.example.market.persistence;

import com.example.market.domain.Purchase;
import com.example.market.domain.repository.PurchaseRepository;
import com.example.market.persistence.crud.CompraCrudRepository;
import com.example.market.persistence.entity.Compra;
import com.example.market.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class CompraRepository implements PurchaseRepository {

    @Autowired
    private CompraCrudRepository compraCrudRepository;

    //anotamos con autowired porque spring ya registro en su contenedor el mapper purchase como componente
    @Autowired
    private PurchaseMapper mapper;

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByCliente(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(compras -> mapper.toPurchases(compras));
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);
        //asignamos a cada producto a la compra porque así los productos sabrán a que compra pertenecen después de haber sido convertido desde PURCHASE
        compra.getProductos().forEach(producto -> producto.setCompra(compra));
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }
}

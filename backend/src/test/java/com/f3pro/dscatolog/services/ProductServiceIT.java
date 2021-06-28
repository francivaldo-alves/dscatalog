package com.f3pro.dscatolog.services;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.f3pro.dscatolog.dto.ProductDTO;
import com.f3pro.dscatolog.repositories.ProductRepository;
import com.f3pro.dscatolog.services.exceptions.ResourceNotFoundExeption;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 500L;
		countTotalProducts = 25L;
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}

	@Test
	public void deleteShouldThrowEmptyResourceNotFoundExeptionIdDoesExists() {
		Assertions.assertThrows(ResourceNotFoundExeption.class, () -> {
			service.delete(nonExistingId);
		});

	}

	@Test
	public void findAllPAgedShouldReturnPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}

	@Test
	public void findAllPAgedShouldReturnPageWhenPageDoesExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);

		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertTrue(result.isEmpty());

	}

	@Test
	public void findAllPAgedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("Name"));

		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
		
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());

		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}

}

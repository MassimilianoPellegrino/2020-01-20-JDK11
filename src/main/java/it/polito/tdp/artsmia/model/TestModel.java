package it.polito.tdp.artsmia.model;

import java.util.List;

public class TestModel {
	
	public static void main(String[] args) {
		
		Model m = new Model();
		
		m.creaGrafo("Designer");
		List<Author> lista = m.getCammino(3042);
		
		for(Author a: lista)
			System.out.println(a);
		
		System.out.println("Peso: "+m.getBestW());
	}

}
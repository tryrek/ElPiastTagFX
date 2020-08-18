package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Plik {
	String sciezka;
	public Plik(String sciezka) {
		this.sciezka = sciezka;
	}
	
	public ArrayList<String> czytajTxtDoListy(){
		ArrayList<String> lista = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(sciezka));
			String line = reader.readLine();
			boolean utnij=false;
			while (line != null) {
				if(line.contains("VARIABLES:")) {
					utnij=true;
				}
				if(utnij==true)lista.add(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lista.remove(0);
		lista.remove(0);
		return lista;
	}
	public ArrayList<piastTag> makePiastTagList(ArrayList<String>lista){
		ArrayList<piastTag> listaZmiennych = new ArrayList<piastTag>();
		ArrayList<String> nazwa = new ArrayList<String>();
		ArrayList<String> komunikacja = new ArrayList<String>();
		ArrayList<String> typ = new ArrayList<String>();
		ArrayList<String> opis = new ArrayList<String>();
		ArrayList<String> adres = new ArrayList<String>();
		for(String a: lista) {
			String[] splitted = a.split("\\s");
			int numer = Integer.parseInt(splitted[0]); //adres
			String numerString = String.valueOf((numer*2)+1);
			adres.add(numerString);
			nazwa.add(splitted[1]); //nazwa
			opis.add(splitted[1]); //opis
			typ.add("4x_32Bit"); //typ
			komunikacja.add("MODBUS TCP/IP (32-Bit)"); //komunikacja
			if(splitted[2].contains("Enum")&&(splitted[3].contains("Digital"))) {
				adres.add(numerString+".00");
				nazwa.add(splitted[1]+"_1"); //nazwa
				opis.add(splitted[1]); //opis
				typ.add("4x_Bit"); //typ
				komunikacja.add("MODBUS TCP/IP (32-Bit)"); //komunikacja				
			}
			else if(splitted[2].contains("Fixed")&&(splitted[3].contains("Digital"))) {
				adres.add(numerString+".00");
				nazwa.add(splitted[1]+"_1"); //nazwa
				opis.add(splitted[1]); //opis
				typ.add("4x_Bit"); //typ
				komunikacja.add("MODBUS TCP/IP (32-Bit)"); //komunikacja				
			}
		}
		for(int i =0;i<nazwa.size();i++) {
			piastTag tag = new piastTag(nazwa.get(i), komunikacja.get(i), typ.get(i), adres.get(i), opis.get(i));
			listaZmiennych.add(tag);
		}
		
		
		return listaZmiennych;
	}

}

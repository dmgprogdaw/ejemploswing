import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Contacts extends TreeMap<String, String> {
	
	File file;
	JFileChooser jf = new JFileChooser();
	
	public String exec(String cmd) {
		String result = null;
		Scanner s = new Scanner(cmd);
		int estado = 0;
		String token;
		String nombre = null;
		while (estado != 7) {
			switch (estado) {
			case 0:
				try {
					token = s.skip("buscar|borrar|\\p{L}+(\\s+\\p{L}+)*").match().group();
					if (token.equals("buscar"))
						estado = 2;
					else if (token.equals("borrar"))
						estado = 5;
					else {
						nombre = token;
						estado = 1;
					}
				} catch (NoSuchElementException e) {
					result = "Se esperaba 'buscar' o un nombre";
					estado = 7;
				}
				break;
			case 1:
				try {
					s.skip("-");
					estado = 3;
				} catch (NoSuchElementException e) {
					result = "Se esperaba '-'";
					estado = 7;
				}
				break;
			case 2:
				try {
					s.skip(":");
					estado = 4;
				} catch (NoSuchElementException e) {
					result = "Se esperaba ':'";
					estado = 7;
				}
				break;
			case 3:
				try {
					token = s.skip("\\d{9}").match().group();
					put(nombre, token);
					estado = 7;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un teléfono";
					estado = 7;
				}
				break;
			case 4:
				try {
					token = s.skip(
							"[a-zA-ZáéíóúÁÉÍÓÚ]+\\s+([a-zA-ZáéíóúÁÉÍÓÚ]+\\s+)*[a-zA-ZáéíóúÁÉÍÓÚ]+|[a-zA-ZáéíóúÁÉÍÓÚ]+")
							.match().group();
					String telefono = get(token);
					if (telefono != null) 
						result = token + " -> " + telefono;
					else 
						result = token + " no se encuentra en la agenda";
					estado = 7;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un nombre";
					estado = 7;
				}
				break;
			case 5: 
				try {
					s.skip(":");
					estado = 6;
				} catch (NoSuchElementException e) {
					result = "Se esperaba ':'";
					estado = 7;
				}
				break;
			case 6:
				try {
					token = s.skip(
							"[a-zA-ZáéíóúÁÉÍÓÚ]+\\s+([a-zA-ZáéíóúÁÉÍÓÚ]+\\s+)*[a-zA-ZáéíóúÁÉÍÓÚ]+|[a-zA-ZáéíóúÁÉÍÓÚ]+")
							.match().group();
					String telefono = get(token);
					if (telefono != null) {
						remove(token);
						result = token + " ha sido borrado" ;
					}
					else {
						result = token + " no se encuentra en la agenda";
					}
					estado = 7;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un nombre";
					estado = 7;
				}
			}
		}
		
		
		return result;
	}
	
	public void load(File file) {
		int selection = jf.showOpenDialog(null); 
		if (selection == JFileChooser.APPROVE_OPTION) {
			file = jf.getSelectedFile();
		}
	}
	
	public void save() {
		int ruta = jf.showSaveDialog(null);
		if (file == null) {
			if (ruta == JFileChooser.APPROVE_OPTION)
				file = jf.getSelectedFile();
		}
		else {
			if (ruta == JFileChooser.APPROVE_OPTION)
				file = jf.getSelectedFile();
		}		
	}
	
	public void saveas(File file) {
		save();
	}
}

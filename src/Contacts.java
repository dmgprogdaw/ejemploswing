import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class Contacts extends TreeMap<String, String> {
	
	File file;
	
	public String exec(String cmd) {
		String result = null;
		Scanner s = new Scanner(cmd);
		int estado = 0;
		String token;
		String nombre = null;
		while (estado != 5) {
			switch (estado) {
			case 0:
				try {
					token = s.skip("buscar|\\p{L}+(\\s+\\p{L}+)*").match().group();
					if (token.equals("fin"))
						estado = 5;
					else if (token.equals("buscar"))
						estado = 2;
					else {
						nombre = token;
						estado = 1;
					}
				} catch (NoSuchElementException e) {
					result = "Se esperaba 'buscar' o un nombre";
					estado = 5;
				}
				break;
			case 1:
				try {
					s.skip("-");
					estado = 3;
				} catch (NoSuchElementException e) {
					result = "Se esperaba '-'";
					estado = 5;
				}
				break;
			case 2:
				try {
					s.skip(":");
					estado = 4;
				} catch (NoSuchElementException e) {
					result = "Se esperaba ':'";
					estado = 5;
				}
				break;
			case 3:
				try {
					token = s.skip("\\d{9}").match().group();
					put(nombre, token);
					estado = 5;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un teléfono";
					estado = 5;
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
					estado = 5;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un nombre";
					estado = 5;
				}
				break;
			}
		}
		
		
		return result;
	}
	
	public void load(File file) {
		
	}
	
	public void save() {
		
	}
	
	public void saveas(File file) {
		this.file = file;
		save();
	}
}

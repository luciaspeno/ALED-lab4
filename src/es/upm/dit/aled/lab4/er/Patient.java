package es.upm.dit.aled.lab4.er;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.upm.dit.aled.lab4.er.gui.EmergencyRoomGUI;
import es.upm.dit.aled.lab4.er.gui.Position2D;

/**
 * Models a patient in a hospital ER. Each Patient is characterized by its
 * number (which must be unique), its current location and a protocol. The
 * protocol is a List of Transfers. Each Patient also has an index to indicate
 * at which point of the protocol they are at the current time.
 * 
 * Patients are Threads, and therefore must implement the run() method.
 * 
 * Each Patient is represented graphically by a dot of diameter 10 px, centered
 * in a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Patient extends Thread {

	private int number;
	private List<Transfer> protocol;
	private int indexProtocol;
	private Area location;
	private Position2D position;
	private Color color;

	/**
	 * Builds a new Patient.
	 * 
	 * @param numbre          The number of the Patient.
	 * @param initialLocation The initial location of the Patient.
	 */
	public Patient(int number, Area initialLocation) {
		this.number = number;
		this.protocol = new ArrayList<Transfer>();
		this.indexProtocol = 0;
		this.location = initialLocation;
		this.position = initialLocation.getPosition();
		this.color = Color.GRAY; // Default color
	}

	/**
	 * Returns the number of the Patient.
	 * 
	 * @return The number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the protocol of the Patient.
	 * 
	 * @return The protocol.
	 */
	public List<Transfer> getProtocol() {
		return protocol;
	}

	/**
	 * Returns the current location of the Patient.
	 * 
	 * @return The current location.
	 */
	public Area getLocation() {
		return location;
	}

	/**
	 * Changes the current location of the Patient.
	 * 
	 * @param location The new location.
	 */
	public void setLocation(Area location) {
		this.location = location;
	}

	/**
	 * Returns the position of the Patient in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Changes the position of the Patient in the GUI.
	 * 
	 * @param position The new position.
	 */
	public void setPosition(Position2D position) {
		this.position = position;
	}

	/**
	 * Returns the color of Patient in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Patient in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Adds a new Transfer at the end of the Patient's protocol.
	 * 
	 * @param transfer The new Transfer.
	 */
	public void addToProtocol(Transfer transfer) {
		this.protocol.add(transfer);
	}

	/**
	 * Advances the Patient's protocol. The Patient is moved to the new Area, the
	 * movement is animated by the GUI and the index is increased by one.
	 */
	private void advanceProtocol() {
		// TODO
		//El protocolo tiene los pasos que debe seguir el paciente y esta formado por objetos transfer
		//Los transfer tienen el siguiente area y el tiempo que pasan los pacientes en el area
		//Con la clase Transfer podemos mover al paciente a ese area
		
		//El paso en el que estoy me dice el area destino
		Transfer alSiguienteArea= this.protocol.get(this.indexProtocol); 
		
		//Debo actualizar la location, la nueva sera el area "to"
		this.location = alSiguienteArea.getTo();

		//Debo actualizar la posicion de mi lista de transfers para poder luego avanzar al siguiente
		this.indexProtocol ++;
		
		//Ahora debo simular usando GUI para ver el paciente moverse
		//Quien lo dibuja es animateTransfer(Paciente p, Transfer t)-> es ESTE paciente-> this 
		//y el transfer será el que toca ahora-> alSiguienteAre
		
		//No puedo poner: EmergencyRoomGUI.animateTransfer(this, alSiguienteArea);
		//Como animateTansfer no es estático necesita un objeto para ser llamado ¿Quien es ese objeto?
		//EmergencyRoomGUI.getInstance() devuelve el único objeto real que es la ventana que vemos en pantalla.
		EmergencyRoomGUI.getInstance().animateTransfer(this, alSiguienteArea);
		
		//Trazas: imprimimos de donde a donde se mueve el paciente para saberlo
		System.out.println("Paciente " + number + " se mueve de " +
							location.getName() + " a " + alSiguienteArea.getTo().getName());
	}
 
	/**
	 * Simulates the treatment of the Patient at its current location. Therefore,
	 * the Patient must spend at this method the amount of time specified in such
	 * Area.
	 */
	private void attendedAtLocation() {
		// TODO
		try{
			sleep(this.location.getTime());
		}catch(InterruptedException e){
			
		}
	}

	/**
	 * Executes the Patient's behavior. It follows their protocol by being attended
	 * at the current location and then moving onto the next, until the last step of
	 * the protocol is reached. At that point, the Patient is removed from the GUI.
	 */
	@Override
	public void run() {
		// TODO
		
	//Esta clase NO es una hebra sino es lo que HACE una hebra. Es decir, muchos pacientes realizan estas TAREAS
	//CADA OBJETO PACIENTE SÍ ES UNA HEBRA
	//Por tanto, el metodo run() lo hace las distintas hebras/pacientes
	//Utiliza los métodos que scabamos de programar
	
		
		do {
			//Un paciente hace:
			//1)Ser atendido en la ubicación actual: es decir el metodo attendedLocation()
						//Este metodo bloquea al paciente el tiempo que sea necesario en ese area mientras es atendido
			this.attendedAtLocation();
			
			//2)Avanzar al siguiente paso: método advanceProtocol()
			this.advanceProtocol();
		}while(indexProtocol<protocol.size());//3)Repetir el proceso hasta acabar la lista de tareas
		
		this.attendedAtLocation();	//cada trasnfer tiene el siguiente area donde es atendido pero falta el primero o el ultimo atendido

		 
		EmergencyRoomGUI.getInstance().removePatient(this);
		
		//Trazas
		System.out.println ("El paciente: "+this.number+" ha terminado de ser atendido en "+ this.location.getName());
		
	
	}

}
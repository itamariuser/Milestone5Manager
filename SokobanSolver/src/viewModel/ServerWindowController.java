package viewModel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.server.ServerModel;

public class ServerWindowController extends Observable implements Observer{//ViewModel

	@FXML
	TableView<InetAddress> tableView;
	
	ServerModel model;
	boolean isInit;
	
	@FXML
	Label hint;
	
	@FXML
	RadioButton awaitingRadioButton;
	
	@FXML
	RadioButton hanledRadioButton;
	
	private ObservableList<InetAddress> awaitingList;
	private ObservableList<InetAddress> handledHistory; 
	
	private boolean showAwaiting;
	
	private HashMap<InetAddress,Date> addTimes;
	private HashMap<InetAddress,Date> handledTimes;
	
	private ArrayList<InetAddress> stubAwaiting;
	private ArrayList<InetAddress> stubHandled;
	
	public ServerWindowController(){//ServerModel model) {
		/*this.model=model;
		model.addObserver(this);*/
		awaitingList=FXCollections.observableArrayList();
		handledHistory=FXCollections.observableArrayList();
		/*for (Socket socket : model.getAwaitingClients()) {
			awaitingList.add(socket.getInetAddress());
			addTimes.put(socket.getInetAddress(),ZonedDateTime.now());
		}
		for (Socket socket : model.getHandledClients()) {
			handledHistory.add(socket.getInetAddress());
			handledTimes.put(socket.getInetAddress(),ZonedDateTime.now());
		}*/
		isInit=false;
		showAwaiting=false;
		
		stubAwaiting=new ArrayList<>();
		stubHandled=new ArrayList<>();
		
		addTimes=new HashMap<>();
		handledTimes=new HashMap<>();
		
		
		//STUB: SIMULATE GETTING ADDRESSES FROM SERVER
		InetAddress i1=null;
		try {
			i1 = InetAddress.getLocalHost();

			stubAwaiting.add(i1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		awaitingList.addAll(stubAwaiting);
		
		InetAddress i2=null;
		try {
			i2 = InetAddress.getLocalHost();

			stubHandled.add(i2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		handledHistory.addAll(stubHandled);
		addTimes.put(i1, new Date());
		handledTimes.put(i2, new Date());
		//END OF STUB
		
		for (InetAddress inetAddress : awaitingList) {
			addTimes.put(inetAddress, new Date());
		}
		
		for (InetAddress inetAddress : handledHistory) {
			addTimes.put(inetAddress, new Date());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void init()
	{
		isInit=true;
		
		TableColumn<InetAddress,String> hostCol=new TableColumn<>("Host Name");
		hostCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InetAddress,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<InetAddress, String> arg0) {
				return new SimpleStringProperty(arg0.getValue().getHostName());
			}
		});
		
		TableColumn<InetAddress,String> ipCol=new TableColumn<>("IP Address");
		ipCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InetAddress,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<InetAddress, String> arg0) {
				return new SimpleStringProperty(arg0.getValue().getHostAddress());
			}
		});
		
		TableColumn<InetAddress,String> dateCol=new TableColumn<>("Time");
		dateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InetAddress,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<InetAddress, String> arg0) {
				if(showAwaiting)
					return new SimpleStringProperty(addTimes.get(arg0.getValue()).toString());
				else
				{
					InetAddress s=arg0.getValue();
					String temp=(handledTimes.get(s)).toString();
					return new SimpleStringProperty(temp);
				}
					
			}
		});
		
		tableView.getColumns().clear();
		tableView.getColumns().addAll(hostCol,ipCol,dateCol);
	}


	@Override
	public void update(Observable o, Object arg) {
		if(o==model)
		{
			if(arg instanceof List)
			{
				Object obj=((List<?>) arg).get(0);
				Object str=((List<?>) arg).get(1);
				if(obj instanceof InetAddress &&  str instanceof String )
				{
					String s=(String)str;
					InetAddress i=(InetAddress)obj;
					if(s.startsWith("add"))
					{
						if(s.endsWith("handledClients"))
						{
							handledHistory.add(i);
						}
						else if(s.endsWith("awaitingClients"))
						{
							awaitingList.add(i);
						}
					}
					if(s.startsWith("remove"))
					{
						if(s.endsWith("handledClients"))
						{
							handledHistory.remove(i);
						}
						else if(s.endsWith("awaitingClients"))
						{
							awaitingList.remove(i);
						}
					}
				}
			}
		}
	}
	
	public void kickUser()
	{
		model.RemoveFromAwaitingClients(tableView.getSelectionModel().getSelectedItem());
	}
	
	public void showWaiting()
	{
		if(!isInit) init();
		hanledRadioButton.setSelected(false);
		
		tableView.setItems(awaitingList);
		FilteredList<InetAddress> data=new FilteredList<>(awaitingList);
		data.setPredicate(addr -> awaitingList.contains(addr));
		
		setTableData(data);
	}
	
	public void showHistory()
	{
		if(!isInit) init();
		awaitingRadioButton.setSelected(false);
		
		tableView.setItems(handledHistory);
		FilteredList<InetAddress> data=new FilteredList<>(handledHistory);
		data.setPredicate(addr -> handledHistory.contains(addr));
		
		setTableData(data);
	}
	
	private void setTableData(FilteredList<InetAddress> data)
	{
		SortedList<InetAddress> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);
	}
	
	public void shutdown()
	{
		
	}
}

package client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * List Implementation
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
@WebServiceClient(name = "ListImpService", targetNamespace = "http://service/", wsdlLocation = "http://localhost:8080/ListImp?wsdl")
public class ListImpService extends Service{

	protected ListImpService(URL wsdlDocumentLocation, QName serviceName) {
		super(wsdlDocumentLocation, serviceName);
		// TODO Auto-generated constructor stub
	}

	private final static URL LISTIMPSERVICE_WSDL_LOCATION;
	static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ListImp?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        LISTIMPSERVICE_WSDL_LOCATION = url;
    }

	public static URL getListImpServiceWsdlLocation() {
		return  LISTIMPSERVICE_WSDL_LOCATION;
	}
	
	public  ListImpService() {
        super(LISTIMPSERVICE_WSDL_LOCATION, new QName("http://service/", "ListImpService"));
    }
	
	@WebEndpoint(name = "ListImpPort")
    public ListImp getListImpPort() {
        return (ListImp)super.getPort(new QName("http://service/", "ListImpPort"), ListImp.class);
    }

    /**
     * 
     * @param features 
     * @return
     */
    @WebEndpoint(name = "ListImpPort")
    public ListImp getClientListPort(WebServiceFeature... features) {
        return (ListImp)super.getPort(new QName("http://service/", "ListImpPort"), ListImp.class, features);
    }

}

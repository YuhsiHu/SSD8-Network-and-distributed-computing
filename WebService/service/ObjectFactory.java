
package com.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DeleteItemResponse_QNAME = new QName("http://www.service.com", "deleteItemResponse");
    private final static QName _ClearItem_QNAME = new QName("http://www.service.com", "clearItem");
    private final static QName _AddItemResponse_QNAME = new QName("http://www.service.com", "addItemResponse");
    private final static QName _UserRegister_QNAME = new QName("http://www.service.com", "userRegister");
    private final static QName _DeleteItem_QNAME = new QName("http://www.service.com", "deleteItem");
    private final static QName _UserRegisterResponse_QNAME = new QName("http://www.service.com", "userRegisterResponse");
    private final static QName _ClearItemResponse_QNAME = new QName("http://www.service.com", "clearItemResponse");
    private final static QName _QueryItem_QNAME = new QName("http://www.service.com", "queryItem");
    private final static QName _QueryItemResponse_QNAME = new QName("http://www.service.com", "queryItemResponse");
    private final static QName _AddItem_QNAME = new QName("http://www.service.com", "addItem");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddItem }
     * 
     */
    public AddItem createAddItem() {
        return new AddItem();
    }

    /**
     * Create an instance of {@link QueryItem }
     * 
     */
    public QueryItem createQueryItem() {
        return new QueryItem();
    }

    /**
     * Create an instance of {@link QueryItemResponse }
     * 
     */
    public QueryItemResponse createQueryItemResponse() {
        return new QueryItemResponse();
    }

    /**
     * Create an instance of {@link ClearItemResponse }
     * 
     */
    public ClearItemResponse createClearItemResponse() {
        return new ClearItemResponse();
    }

    /**
     * Create an instance of {@link UserRegisterResponse }
     * 
     */
    public UserRegisterResponse createUserRegisterResponse() {
        return new UserRegisterResponse();
    }

    /**
     * Create an instance of {@link DeleteItem }
     * 
     */
    public DeleteItem createDeleteItem() {
        return new DeleteItem();
    }

    /**
     * Create an instance of {@link UserRegister }
     * 
     */
    public UserRegister createUserRegister() {
        return new UserRegister();
    }

    /**
     * Create an instance of {@link AddItemResponse }
     * 
     */
    public AddItemResponse createAddItemResponse() {
        return new AddItemResponse();
    }

    /**
     * Create an instance of {@link ClearItem }
     * 
     */
    public ClearItem createClearItem() {
        return new ClearItem();
    }

    /**
     * Create an instance of {@link DeleteItemResponse }
     * 
     */
    public DeleteItemResponse createDeleteItemResponse() {
        return new DeleteItemResponse();
    }

    /**
     * Create an instance of {@link TodoList }
     * 
     */
    public TodoList createTodoList() {
        return new TodoList();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "deleteItemResponse")
    public JAXBElement<DeleteItemResponse> createDeleteItemResponse(DeleteItemResponse value) {
        return new JAXBElement<DeleteItemResponse>(_DeleteItemResponse_QNAME, DeleteItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "clearItem")
    public JAXBElement<ClearItem> createClearItem(ClearItem value) {
        return new JAXBElement<ClearItem>(_ClearItem_QNAME, ClearItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "addItemResponse")
    public JAXBElement<AddItemResponse> createAddItemResponse(AddItemResponse value) {
        return new JAXBElement<AddItemResponse>(_AddItemResponse_QNAME, AddItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRegister }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "userRegister")
    public JAXBElement<UserRegister> createUserRegister(UserRegister value) {
        return new JAXBElement<UserRegister>(_UserRegister_QNAME, UserRegister.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "deleteItem")
    public JAXBElement<DeleteItem> createDeleteItem(DeleteItem value) {
        return new JAXBElement<DeleteItem>(_DeleteItem_QNAME, DeleteItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "userRegisterResponse")
    public JAXBElement<UserRegisterResponse> createUserRegisterResponse(UserRegisterResponse value) {
        return new JAXBElement<UserRegisterResponse>(_UserRegisterResponse_QNAME, UserRegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "clearItemResponse")
    public JAXBElement<ClearItemResponse> createClearItemResponse(ClearItemResponse value) {
        return new JAXBElement<ClearItemResponse>(_ClearItemResponse_QNAME, ClearItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "queryItem")
    public JAXBElement<QueryItem> createQueryItem(QueryItem value) {
        return new JAXBElement<QueryItem>(_QueryItem_QNAME, QueryItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "queryItemResponse")
    public JAXBElement<QueryItemResponse> createQueryItemResponse(QueryItemResponse value) {
        return new JAXBElement<QueryItemResponse>(_QueryItemResponse_QNAME, QueryItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "addItem")
    public JAXBElement<AddItem> createAddItem(AddItem value) {
        return new JAXBElement<AddItem>(_AddItem_QNAME, AddItem.class, null, value);
    }

}

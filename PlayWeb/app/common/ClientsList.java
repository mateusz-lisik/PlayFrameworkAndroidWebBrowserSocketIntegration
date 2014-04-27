package common;


import java.util.*;

public class ClientsList implements List<MyWebSocket> {
    private static ClientsList instance;
    public List<MyWebSocket> list;

    private ClientsList() {
        list = new ArrayList<>(10);
    }

    public static ClientsList getInstance() {
        if(ClientsList.instance == null)
            ClientsList.instance = new ClientsList();
        return ClientsList.instance;
    }

    public List<MyWebSocket> get(SocketClientType deviceType) {
        List<MyWebSocket> results = new ArrayList<>(10);
        for (MyWebSocket socket : list) {
            if (socket.type == deviceType) {
                results.add(socket);
            }
        }
        return results;
    }

    public static void sendMessageToEveryClientInGroup(SocketClientType type, String message) {
        List<MyWebSocket> clients = ClientsList.getInstance().get(type);
        for (MyWebSocket socket : clients) {
            socket.out.write(message);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<MyWebSocket> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(MyWebSocket myWebSocket) {
        return list.add(myWebSocket);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends MyWebSocket> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends MyWebSocket> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public MyWebSocket get(int index) {
        return list.get(index);
    }

    @Override
    public MyWebSocket set(int index, MyWebSocket element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, MyWebSocket element) {
        list.add(index, element);
    }

    @Override
    public MyWebSocket remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<MyWebSocket> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<MyWebSocket> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<MyWebSocket> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
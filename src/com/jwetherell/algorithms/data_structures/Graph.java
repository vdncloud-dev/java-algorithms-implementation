package com.jwetherell.algorithms.data_structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Graph. Could be directed or undirected depending on the TYPE enum. A graph is
 * an abstract representation of a set of objects where some pairs of the
 * objects are connected by links.
 * 
 * http://en.wikipedia.org/wiki/Graph_(mathematics)
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
@SuppressWarnings("unchecked")
public class Graph<T extends Comparable<T>> {

    private List<Vertex<T>> verticies = new ArrayList<Vertex<T>>();
    private List<Edge<T>> edges = new ArrayList<Edge<T>>();

    public enum TYPE {
        DIRECTED, UNDIRECTED
    }

    private TYPE type = TYPE.UNDIRECTED;

    public Graph() { }

    public Graph(TYPE type) {
        this.type = type;
    }

    public Graph(Graph<T> g) {
        // Deep copies

        type = g.getType();

        // Copy the vertices (which copies the edges)
        for (Vertex<T> v : g.getVerticies())
            this.verticies.add(new Vertex<T>(v));

        // Update the object references
        for (Vertex<T> v : this.verticies) {
            for (Edge<T> e : v.getEdges())
                this.edges.add(e);
        }
    }

    public Graph(Collection<Vertex<T>> verticies, List<Edge<T>> edges) {
        this(TYPE.UNDIRECTED, verticies, edges);
    }

    public Graph(TYPE type, Collection<Vertex<T>> verticies, List<Edge<T>> edges) {
        this(type);

        this.verticies.addAll(verticies);
        this.edges.addAll(edges);

        for (Edge<T> e : edges) {
            final Vertex<T> from = e.from;
            final Vertex<T> to = e.to;

            if (!this.verticies.contains(from) || !this.verticies.contains(to))
                continue;

            from.addEdge(e);
            if (this.type == TYPE.UNDIRECTED) {
                Edge<T> reciprical = new Edge<T>(e.cost, to, from);
                to.addEdge(reciprical);
                this.edges.add(reciprical);
            }
        }
    }

    public TYPE getType() {
        return type;
    }

    public List<Vertex<T>> getVerticies() {
        return verticies;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Vertex<T> v : verticies)
            builder.append(v.toString());
        return builder.toString();
    }

    public static class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>> {

        private T value = null;
        private int weight = 0;
        private List<Edge<T>> edges = new ArrayList<Edge<T>>();

        public Vertex(T value) {
            this.value = value;
        }

        public Vertex(T value, int weight) {
            this(value);
            this.weight = weight;
        }

        /** Deep copies edges **/
        public Vertex(Vertex<T> vertex) {
            this(vertex.value, vertex.weight);

            this.edges = new ArrayList<Edge<T>>();
            for (Edge<T> e : vertex.edges)
                this.edges.add(new Edge<T>(e));
        }

        public T getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void addEdge(Edge<T> e) {
            edges.add(e);
        }

        public List<Edge<T>> getEdges() {
            return edges;
        }

        public boolean pathTo(Vertex<T> v) {
            for (Edge<T> e : edges) {
                if (e.to.equals(v))
                    return true;
            }
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int code = this.value.hashCode() + this.weight + this.edges.size();
            return 31 * code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object v1) {
            if (!(v1 instanceof Vertex))
                return false;

            final Vertex<T> v = (Vertex<T>) v1;

            final boolean weightEquals = this.weight == v.weight;
            if (!weightEquals)
                return false;

            final boolean edgesSizeEquals = this.edges.size() == v.edges.size();
            if (!edgesSizeEquals)
                return false;

            final boolean valuesEquals = this.value.equals(v.value);
            if (!valuesEquals)
                return false;

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Vertex<T> v) {
            if (this.value == null || v.value == null)
                return -1;
            return this.value.compareTo(v.value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Value=").append(value).append(" weight=").append(weight).append("\n");
            for (Edge<T> e : edges)
                builder.append("\t").append(e.toString());
            return builder.toString();
        }
    }

    public static class Edge<T extends Comparable<T>> implements Comparable<Edge<T>> {

        private Vertex<T> from = null;
        private Vertex<T> to = null;
        private int cost = 0;

        public Edge(int cost, Vertex<T> from, Vertex<T> to) {
            if (from == null || to == null)
                throw (new NullPointerException("Both 'to' and 'from' Verticies need to be non-NULL."));

            this.cost = cost;
            this.from = from;
            this.to = to;
        }

        public Edge(Edge<T> e) {
            this(e.cost, e.from, e.to);
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Vertex<T> getFromVertex() {
            return from;
        }

        public Vertex<T> getToVertex() {
            return to;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int cost = (this.cost * (this.getFromVertex().hashCode() * this.getToVertex().hashCode())); 
            return 31 * cost;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object e1) {
            if (!(e1 instanceof Edge))
                return false;

            final Edge<T> e = (Edge<T>) e1;

            final boolean costs = this.cost == e.cost;
            if (!costs)
                return false;

            final boolean froms = this.from.equals(e.from);
            if (!froms)
                return false;

            final boolean tos = this.to.equals(e.to);
            if (!tos)
                return false;

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Edge<T> e) {
            if (this.cost < e.cost)
                return -1;
            if (this.cost > e.cost)
                return 1;
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ").append(from.value).append("(").append(from.weight).append(") ").append("]").append(" -> ")
                   .append("[ ").append(to.value).append("(").append(to.weight).append(") ").append("]").append(" = ").append(cost).append("\n");
            return builder.toString();
        }
    }

    public static class CostVertexPair<T extends Comparable<T>> implements Comparable<CostVertexPair<T>> {

        private int cost = Integer.MAX_VALUE;
        private Vertex<T> vertex = null;

        public CostVertexPair(int cost, Vertex<T> vertex) {
            if (vertex == null)
                throw (new NullPointerException("vertex cannot be NULL."));

            this.cost = cost;
            this.vertex = vertex;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Vertex<T> getVertex() {
            return vertex;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return 31 * (this.cost * ((this.vertex!=null)?this.vertex.hashCode():1));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object e1) {
            if (!(e1 instanceof CostVertexPair))
                return false;

            final CostVertexPair<?> pair = (CostVertexPair<?>)e1;
            if (this.cost != pair.cost)
                return false;

            if (!this.vertex.equals(pair.vertex))
                return false;

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(CostVertexPair<T> p) {
            if (p == null)
                throw new NullPointerException("CostVertexPair 'p' must be non-NULL.");

            if (this.cost < p.cost)
                return -1;
            if (this.cost > p.cost)
                return 1;
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(vertex.getValue()).append(" (").append(vertex.weight).append(") ").append(" cost=").append(cost).append("\n");
            return builder.toString();
        }
    }

    public static class CostPathPair<T extends Comparable<T>> {

        private int cost = 0;
        private List<Edge<T>> path = null;

        public CostPathPair(int cost, List<Edge<T>> path) {
            if (path == null)
                throw (new NullPointerException("path cannot be NULL."));

            this.cost = cost;
            this.path = path;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public List<Edge<T>> getPath() {
            return path;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            int hash = this.cost;
            for (Edge<T> e : path)
                hash *= e.cost;
            return 31 * hash;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CostPathPair))
                return false;

            final CostPathPair<?> pair = (CostPathPair<?>) obj;
            if (this.cost != pair.cost)
                return false;

            final Iterator<?> iter1 = this.getPath().iterator();
            final Iterator<?> iter2 = pair.getPath().iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                Edge<T> e1 = (Edge<T>) iter1.next();
                Edge<T> e2 = (Edge<T>) iter2.next();
                if (!e1.equals(e2))
                    return false;
            }

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Cost = ").append(cost).append("\n");
            for (Edge<T> e : path)
                builder.append("\t").append(e);
            return builder.toString();
        }
    }
}
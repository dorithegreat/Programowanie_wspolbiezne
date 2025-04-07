import numpy as np
import networkx as nx
import matplotlib.pyplot as plt
import random

class CommunicationNetwork:
    def __init__(self, topology, N, T_max, m):
        self.topology = topology
        self.N = N
        self.T_max = T_max
        self.m = m
        self.graph = nx.Graph()
        self.edges = []
        self.edge_attributes = {}
        self.construct_network()

    def construct_network(self):
        self.graph.add_nodes_from(self.topology['nodes'])
        self.edges = self.generate_edges()
        self.graph.add_edges_from(self.edges)
        self.set_edge_attributes()

    def generate_edges(self):
        edges = []
        for edge in self.topology['edges']:
            edges.append((edge['source'], edge['target']))
            edges.append((edge['target'], edge['source']))  
        return edges

    def set_edge_attributes(self):
        q = 1
        c1 = 30000000 * q
        c2 = 70000000 * q
        c3 = 100000000 * q
        c4 = 67777777 * q #average of all the other edges
        p1 = 0.98
        p2 = 0.98
        p3 = 0.98
        for edge in self.edges:
            if edge in [(0, 1), (1, 0)]:
                c = c1
                p = p1
            elif edge in [(0, 2), (2, 0)]:
                c = c2
                p = p2
            elif edge in [(0, 6), (6, 0)]:
                c = c2
                p = p2
            elif edge in [(1, 7), (7, 1)]:
                c = c3
                p = p3
            elif edge in [(6, 11), (11, 6)]:
                c = c3
                p = p3
            elif edge in [(11, 17), (17, 11)]:
                c = c2
                p = p2
            elif edge in [(2, 3), (3, 2)]:
                c = c3
                p = p3
            elif edge in [(7, 12), (12, 7)]:
                c = c1
                p = p1
            elif edge in [(12, 17), (17, 12)]:
                c = c1
                p = p1
            elif edge in [(3, 8), (8, 3)]:
                c = c3
                p = p3
            elif edge in [(13, 16), (16, 13)]:
                c = c2
                p = p2
            elif edge in [(16, 17), (17, 16)]:
                c = c3
                p = p3
            elif edge in [(3, 4), (4, 3)]:
                c = c2
                p = p2
            elif edge in [(4, 5), (5, 4)]:
                c = c1
                p = p1
            elif edge in [(4, 9), (9, 4)]:
                c = c3
                p = p3
            elif edge in [(9, 14), (14, 9)]:
                c = c3
                p = p3
            elif edge in [(5, 10), (10, 5)]:
                c = c1
                p = p1
            elif edge in [(10, 19), (19, 10)]:
                c = c1
                p = p1
            elif edge in [(10, 14), (14, 10)]:
                c = c1
                p = p1
            elif edge in [(14, 15), (15, 14)]:
                c = c3
                p = p3
            elif edge in [(15, 18), (18, 15)]:
                c = c2
                p = p2
            elif edge in [(18, 16), (16, 18)]:
                c = c2
                p = p2
            elif edge in [(15, 19), (19, 15)]:
                c = c1
                p = p1
            elif edge in [(8, 13), (13, 8)]:
                c = c3
                p = p3
            elif edge in [(1, 12), (12, 1)]:
                c = c2
                p = p2
            elif edge in [(6, 2), (2, 6)]:
                c = c3
                p = p3
            elif edge in [(5, 19), (19, 5)]:
                c = c1
                p = p1
            
            else:
                c = c4
                p = p1
            
            self.edge_attributes[edge] = {'c': c, 'a': 0, 'p': p}

    def set_a(self): #calculates traffic through all the edges from all nodes
        for source in self.graph.nodes:   
            for target in self.graph.nodes:
                
                if source != target:
                    shortest_path = nx.shortest_path(self.graph, source=source, target=target)
                    
                    for i in range(len(shortest_path)-1):
                        
                        edge = (shortest_path[i], shortest_path[i+1])
                        self.edge_attributes[edge]['a'] += self.N[source][target]
                        
                        if self.edge_attributes[edge]['a'] * self.m > self.edge_attributes[edge]['c']:
                            return False
        return True

    def delay(self):
        total_delay = 0
        
        for edge in self.edges:
            c = self.edge_attributes[edge]['c']
            a = self.edge_attributes[edge]['a']
            
            if (c / self.m - a) > 0 :
                total_delay += a / (c / self.m - a) #formula for delay
                
        G = np.sum(self.N)
        T = 1 / G * total_delay
        
        return T

    def add_edge(self, edge):
        self.graph.add_edge(*edge)
        self.edges.append(edge)
        self.set_edge_attributes()

    def remove_edge(self, source, target):
        if (source, target) in self.graph.edges:
            self.graph.remove_edge(source, target)
        elif (target, source) in self.graph.edges:
            self.graph.remove_edge(target, source)
            
    def check_net(self):
        if nx.is_connected(self.graph) == True:
            if self.set_a() == True:
                
                t = self.delay()
                
                if t < T_max:
                    return True
        return False
    
    def loop(self, iterations): #repeats the experiment i times
        s = 0
        for i in range(iterations):
            original_edges = self.edges.copy()  
            
            for edge in self.edges:
                if random.random() > self.edge_attributes[edge]['p']:
                    self.remove_edge(*edge)
                    
            if self.check_net():
                s += 1
                
            self.edges = original_edges.copy()
            self.construct_network()  
            
        return s
            
    

topology = {
    'nodes': range(0, 20),  
    'edges': [
        {'source': 0, 'target': 2}, {'source': 0, 'target': 1}, {'source': 0, 'target': 6},
        {'source': 1, 'target': 7}, {'source': 6, 'target': 11}, {'source': 11, 'target': 17},
        {'source': 2, 'target': 3}, {'source': 7, 'target': 12}, {'source': 12, 'target': 17},
        {'source': 3, 'target': 8}, {'source': 13, 'target': 16}, {'source': 16, 'target': 17},
        {'source': 3, 'target': 4}, {'source': 4, 'target': 5}, {'source': 4, 'target': 9},
        {'source': 9, 'target': 14}, {'source': 5, 'target': 10}, {'source': 10, 'target': 19},
        {'source': 10, 'target': 14}, {'source': 14, 'target': 15}, {'source': 15, 'target': 18},
        {'source': 18, 'target': 16}, {'source': 15, 'target': 19}, {'source': 8, 'target': 13},
        {'source': 11, 'target': 7}, {'source': 13, 'target': 9}, {'source': 8, 'target': 18},
        {'source': 1, 'target': 12}, {'source': 6, 'target': 2}, {'source': 5, 'target': 19},

        {'source': 0, 'target': 10}, {'source': 7, 'target': 16}, {'source': 11, 'target': 15}
        #{'source': 0, 'target': 7}, {'source': 9, 'target': 18}, {'source': 5, 'target': 9}
    ]
}
N = np.zeros((20, 20)) 
a = 1
n1 = 90 *a
n2 =70 *a
n3 = 30 *a
n4 = 40 *a

N[0, 1] = n1
N[1, 0] = n3
N[0, 2] = n2
N[2, 0] = n3 
N[0, 6] = n2
N[6, 0] = n1  
N[1, 7] = N[7, 1] = n2  
N[2, 3] = N[3, 2] = n1 
N[3, 8] = N[8, 3] = n1  
N[3, 4] = N[4, 3] = n3 
N[4, 5] = N[5, 4] = n2  
N[4, 9] = N[9, 4] = n2
N[5, 10] = N[10, 5] = n1  
N[6, 11] = N[11, 6] = n1 
N[7, 12] = N[12, 7] = n3  
N[8, 13] = N[13, 8] = n2  
N[9, 14] = N[14, 9] = n2 
N[10, 19] = N[19, 10] = n1  
N[11, 17] = N[17, 11] = n1 
N[12, 17] = N[17, 12] = n3
N[13, 16] = N[16, 13] = n3
N[14, 15] = N[15, 14] = n1 
N[15, 18] = N[18, 15] = n3
N[16, 17] = N[17, 16] = n3 
N[18, 16] = N[16, 18] = n2 
N[19, 15] = N[15, 19] = n2 

for i in range(N.shape[0]):
    for j in range(N.shape[1]):
        if i!=j:
            if N[i, j] == 0:
                N[i, j] = n4  #fill the rest with n4


T_max = 0.5
m = 12000 #average packet size 

network = CommunicationNetwork(topology, N, T_max, m)
i = 10000
s = network.loop(i)

print(s/i)

plt.figure(figsize=(10, 6))
pos = nx.spring_layout(network.graph)  
nx.draw(network.graph, pos, with_labels=True, node_size=3000, node_color='skyblue', font_size=10, font_weight='bold')

edge_labels = {(u, v): f"c={network.edge_attributes[(u, v)]['c']/1000000}MB" for u, v in network.graph.edges()}

nx.draw_networkx_edge_labels(network.graph, pos, edge_labels=edge_labels)

plt.title("Communication Network")
plt.show()

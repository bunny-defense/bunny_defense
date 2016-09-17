import scalagen as sg
import re

class Packet():
    def __init__(self, name, types):
        self.name  = name
        self.types = types

    def serialization_code(self):
        code = []
        for i in range(len(self.types)):
            prefix = "++ "
            if i == 0:
                prefix = ""
            name, t = self.types[i]
            code.append(prefix + "Serialize.%s(%s)" % (t.lower(), name))
        return code

    def gen(self):
        root = sg.File()
        root.add_package("network.packets")
        obj = root.add_object(self.name)
        obj.add_function("unserialize", [("data", "Array[Byte]")], self.name)
        c = root.add_class(self.name, self.types, "Packet")
        ser = c.add_function("serialize", self.types, "Array[Byte]")
        ser.add_code(self.serialization_code())
        return root.gen()

packet_decl = re.compile("^packet\\s*([A-Z][A-Za-z0-9_]*)$")
packet_type = re.compile("^([a-z][A-Za-z0-9_]*)\\s*([A-Z][A-Za-z0-9\\[\\]_]*)$")

with open("protocol.cfg", "r") as infile:
    name = None
    types = None
    def endpacket():
        with open( name.lower() + ".scala", "w" ) as outfile:
            outfile.write( Packet(name, types).gen() )
    for line in infile:
        line = line.rstrip()
        decl_match = packet_decl.match(line)
        if decl_match:
            if name != None:
                endpacket()
            name = decl_match.group(1)
            types = []
            continue
        type_match = packet_type.match(line)
        if type_match:
            types.append( (type_match.group(1), type_match.group(2)) )
            continue
        if line != "":
            raise Exception("Syntax error: \"%s\"" % line)
    endpacket()


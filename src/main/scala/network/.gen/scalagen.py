
def tab(depth):
    return " " * 4 * depth

class File():
    def __init__(self):
        self.children = []

    def gen(self, depth=0):
        out = []
        for child in self.children:
            out.append(child.gen(depth))
        return "\n".join(out)

    def add_object(self, name):
        obj = Object(name)
        self.children.append(obj)
        return obj

    def add_class(self, name, parameters, superclass=None):
        c = Class(name, parameters, superclass)
        self.children.append(c)
        return c

    def add_package(self, name):
        package = Package(name)
        self.children.append(package)
        return package

class Object():
    def __init__(self, name):
        self.name = name
        self.children = []

    def gen(self, depth=0):
        out = []
        out.append("")
        out.append(tab(depth) + "object %s" % self.name)
        out.append(tab(depth) + "{")
        for child in self.children:
            out.append(child.gen(depth + 1))
        out.append(tab(depth) + "}")
        return "\n".join(out)

    def add_function(self, name, arguments, type):
        func = Function(name, arguments, type)
        self.children.append(func)
        return func

class Class():
    def __init__(self, name, parameters, superclass=None):
        self.name = name
        self.parameters = parameters
        self.superclass = superclass
        self.children = []

    def gen(self, depth=0):
        parameters = []
        for parameter, t in self.parameters:
            parameters.append("%s: %s" % (parameter, t))
        parameters = ", ".join(parameters)
        out = []
        out.append("")
        out.append(tab(depth) + "class %s(%s)" % (self.name, parameters))
        if self.superclass != None:
            out.append(tab(depth) + "extends %s" % self.superclass)
        out.append(tab(depth) + "{")
        for child in self.children:
            out.append(child.gen(depth+1))
        out.append(tab(depth) + "}")
        return "\n".join(out)

    def add_function(self, name, arguments, type):
        func = Function(name, arguments, type)
        self.children.append(func)
        return func

class Function():
    def __init__(self, name, arguments, type):
        self.name = name
        self.arguments = arguments
        self.type = type
        self.children = []

    def gen(self, depth=0):
        arguments = []
        for name, t in self.arguments:
            arguments.append("%s: %s" % (name, t))
        arguments = ", ".join(arguments)
        out = []
        out.append(tab(depth) + "def %s(%s): %s = {" % (self.name, arguments,
            self.type))
        #out.append(tab(depth) + "{")
        for child in self.children:
            out.append(child.gen(depth+1))
        out.append(tab(depth) + "}")
        return "\n".join(out)

    def add_code(self, content):
        code = Code(content)
        self.children.append(code)
        return code

class Package():
    def __init__(self, name):
        self.name = name

    def gen(self, depth=0):
        return tab(depth) + "package %s" % self.name

class Code():
    def __init__(self, content):
        self.content = content

    def gen(self, depth=0):
        tabulate = lambda x: tab(depth) + x
        return "\n".join(map(tabulate, self.content))

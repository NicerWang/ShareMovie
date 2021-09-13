class Node:
    def __str__(self) -> str:
        return "movieIdx:" + str(self.m) + "\n" + "userIdx:" + str(self.u) + "\n" + "score" + str(self.s) + "\n"
    def __init__(self,mi,ui,value):
        self.m = mi
        self.u = ui
        self.s = value
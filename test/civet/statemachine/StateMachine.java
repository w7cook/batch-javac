package statemachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.civet.Civet;
import org.civet.Civet.Compile;


public class StateMachine {

  static StateMachine regexExample1() {
    StateMachine regex = new StateMachine();
    State s1, s2, s3, s4;
    s1 = new State("s1", 1, false);
    s2 = new State("s125", 2, false);
    s3 = new State("s136", 3, true);
    s4 = new State("s14", 4, true);
    s1.connectTo(s2, 'a');
    s1.connectTo(s1, 'b');
    s2.connectTo(s3, 'b');
    s2.connectTo(s2, 'a');
    s3.connectTo(s4, 'b');
    s3.connectTo(s2, 'a');
    s4.connectTo(s2, 'a');
    s4.connectTo(s1, 'b');
    regex.addState(s1).addState(s2).addState(s3).addState(s4);
    regex.setStart(s1);
    return regex;
  }

  static StateMachine doorExample() {
    StateMachine door = new StateMachine();
    State closed, open, locked;
    closed = new State("closed", 1, false);
    open = new State("open", 2, false);
    locked = new State("locked", 3, true);
    open.connectTo(closed, 'c');
    closed.connectTo(open, 'o');
    closed.connectTo(locked, 'l');
    locked.connectTo(closed, 'u');
    door.addState(open).addState(closed).addState(locked);
    door.setStart(open);
    return door;
  }

  @Compile
  public static void main(String[] args) throws IOException {
    StateMachine st = Civet.CT(StateMachine.doorExample());
    long startTime = System.currentTimeMillis();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    st.execute(br);
    long endTime = System.currentTimeMillis();
    System.out.println("time: " + (endTime - startTime));
  }

  public void execute(BufferedReader s) throws IOException {
    State startState = getStart();
    int current = org.civet.Civet.RT(startState.getId());

    System.out.println("Current: "+current);
    System.out.print(">> ");
    int input = Integer.valueOf(s.readLine().charAt(0));
    while (input != -1) {
      current = run(current, input);
      System.out.println("Current: "+current);
      System.out.print(">> ");
      input = Integer.valueOf(s.readLine().charAt(0));
    }
  }

  public int run(int current, int input) throws java.io.IOException {
    for (State st : states) {
      if (st.getId() == current) {
        for (Transition t : st.getOuts()) {
          if (t.getEvent() == input) {
            State to = t.getTo();
            return to.getId();
          }
        }
        return current;
      }
    }
    return current;
  }

  State start;

  public State getStart() {
    return start;
  }

  java.util.List<State> states = new java.util.ArrayList<State>();

  private StateMachine addState(State s) {
    states.add(s);
    return this;
  }

  public void setStart(State start) {
    this.start = start;
  }
}

class State {

  String name;
  java.util.List<Transition> ins = new java.util.ArrayList<Transition>();
  java.util.List<Transition> outs = new java.util.ArrayList<Transition>();
  int id = 0;

  boolean finalState = false;

  public java.util.List<Transition> getOuts() {
    return outs;
  }

  public State(String name, int id, boolean finalState) {
    super();
    this.name = name;
    this.id = id;
    this.finalState = finalState;
  }

  public void connectTo(State s, int ev) {
    new Transition(this, ev, s);
  }

  public boolean isFinalState() {
    return finalState;
  }

  public Transition getRandomTransition() {
    return outs.get(new java.util.Random().nextInt(outs.size()));
  }

  public void setFinalState(boolean finalState) {
    this.finalState = finalState;
  }

  public int getId() {
    return id;
  }

  public java.util.List<Transition> getIns() {
    return ins;
  }

  public String getName() {
    return name;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof State)) {
      return false;
    }
    State other = (State) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  public String toString() {
    return "State: " + this.name;
  }
}

class Transition {

  State from;
  State to;
  int event;

  public Transition(State from, int event, State to) {
    super();
    this.from = from;
    this.to = to;
    this.event = event;
    from.getOuts().add(this);
    to.getIns().add(this);
  }

  public State getFrom() {
    return from;
  }

  public State getTo() {
    return to;
  }

  public int getEvent() {
    return event;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + event;
    result = prime * result + ((from == null) ? 0 : from.hashCode());
    result = prime * result + ((to == null) ? 0 : to.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Transition other = (Transition) obj;
    if (event != other.event)
      return false;
    if (from == null) {
      if (other.from != null)
        return false;
    } else if (!from.equals(other.from))
      return false;
    if (to == null) {
      if (other.to != null)
        return false;
    } else if (!to.equals(other.to))
      return false;
    return true;
  }

}

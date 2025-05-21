with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Numerics.Float_Random; use Ada.Numerics.Float_Random;
with Random_Seeds; use Random_Seeds;
with Ada.Real_Time; use Ada.Real_Time;

with Ada.Containers.Vectors;

procedure  Travelers is

-- Travelers moving on the board

  Nr_Of_Travelers : Integer :=1;

  Min_Steps : constant Integer := 10 ;
  Max_Steps : constant Integer := 100 ;

  Min_Delay : constant Duration := 0.01;
  Max_Delay : constant Duration := 0.05;

-- 2D Board with torus topology

  Board_Width  : constant Integer := 5;
  Board_Height : constant Integer := 5;

-- Timing

  Start_Time : Time := Clock;  -- global startnig time

-- Random seeds for the tasks' random number generators
 
  Seeds : Seed_Array_Type(1..Nr_Of_Travelers) := Make_Seeds(Nr_Of_Travelers);

-- Types, procedures and functions

  -- traces of travelers
  type Position_Type is record	
    X: Integer range 0 .. Board_Width - 1; 
    Y: Integer range 0 .. Board_Height - 1; 
  end record;

  type Trace_Type is record 	      
    Time_Stamp:  Duration;	      
    Id : Integer;
    Position: Position_Type;      
    Symbol: Character;	      
  end record;	      

  type Trace_Array_type is  array(0 .. Max_Steps) of Trace_Type;

  type Traces_Sequence_Type is record
    Last: Integer := -1;
    Trace_Array: Trace_Array_type ;
  end record; 


  procedure Print_Trace( Trace : Trace_Type ) is
    Symbol : String := ( ' ', Trace.Symbol );
  begin
    Put_Line(
        Duration'Image( Trace.Time_Stamp ) & " " &
        Integer'Image( Trace.Id ) & " " &
        Integer'Image( Trace.Position.X ) & " " &
        Integer'Image( Trace.Position.Y ) & " " &
        ( ' ', Trace.Symbol ) -- print as string to avoid: '
      );
  end Print_Trace;

  procedure Print_Traces( Traces : Traces_Sequence_Type ) is
  begin
    for I in 0 .. Traces.Last loop
      Print_Trace( Traces.Trace_Array( I ) );
    end loop;
  end Print_Traces;

  -- task Printer collects and prints reports of traces
  task Printer is
    entry Report( Traces : Traces_Sequence_Type );
  end Printer;
  
  task body Printer is 
  begin
    loop
      accept Report( Traces : Traces_Sequence_Type ) do
      Print_Traces( Traces );
      end Report;
    end loop;
  end Printer;

  -- Postitions on the board


	   
  task type Board_Cell_Task_Type is
   
    entry Occupy;
    entry Leave;
    entry Stop;

    entry Add_Wildcard(Id : Integer);
    entry Remove_Wildcard;
    entry Request_Wildcard_Move(Id : Integer);

  end Board_Cell_Task_Type;

  type Board_Cell_Access is access all Board_Cell_Task_Type;

  
  -- Board as a 2D array of cell tasks
  type Board_Type is array (0 .. Board_Width - 1, 0 .. Board_Height - 1) of Board_Cell_Access;
  Board : Board_Type;



  -- travelers
  type Traveler_Type is record
    Id: Integer;
    Symbol: Character;
    Position: Position_Type;    
  end record;

  task type Traveler_Task_Type is	
    entry Init(Id: Integer; Seed: Integer; Symbol: Character);
    entry Start;
  end Traveler_Task_Type;	

  task body Traveler_Task_Type is
    G : Generator;
    Traveler : Traveler_Type;
    Time_Stamp : Duration;
    Nr_of_Steps: Integer;
    Traces: Traces_Sequence_Type; 

    procedure Store_Trace is
    begin  
      Traces.Last := Traces.Last + 1;
      Traces.Trace_Array( Traces.Last ) := ( 
          Time_Stamp => Time_Stamp,
          Id => Traveler.Id,
          Position => Traveler.Position,
          Symbol => Traveler.Symbol
        );
    end Store_Trace;

   function Make_Step return Boolean is
         Old_Position : Position_Type := Traveler.Position;
         New_Position : Position_Type := Old_Position;
         N : Integer := Integer( Float'Floor(4.0 * Random(G)) );
      begin
         case N is
            when 0 => New_Position.Y := (Old_Position.Y + Board_Height - 1) mod Board_Height;
            when 1 => New_Position.Y := (Old_Position.Y + 1) mod Board_Height;
            when 2 => New_Position.X := (Old_Position.X + Board_Width - 1) mod Board_Width;
            when 3 => New_Position.X := (Old_Position.X + 1) mod Board_Width;
            when others =>  return False;
         end case;

         -- Try to occupy the new cell with timeout
         select
            Board(New_Position.X, New_Position.Y).Occupy;
            Traveler.Position := New_Position;
            Time_Stamp := To_Duration(Clock - Start_Time);
            Store_Trace;
            Board(Old_Position.X, Old_Position.Y).Leave;
            return True;
         or
            delay Max_Delay;
            --Put_Line("deadlock "& Integer'Image( Traveler.Id )& " " &  Integer'Image( New_Position.X ) & " " & Integer'Image( New_Position.Y ));
            return False;  
         end select;

      end Make_Step;

   Done : Boolean := False;
  begin
    accept Init(Id: Integer; Seed: Integer; Symbol: Character) do
      Reset(G, Seed); 
      Traveler.Id := Id;
      Traveler.Symbol := Symbol;
      
      -- Random initial position:
      Traveler.Position := (
          X => Integer( Float'Floor( Float( Board_Width )  * Random(G)  ) ),
          Y => Integer( Float'Floor( Float( Board_Height ) * Random(G) ) )          
        );

      loop
         exit when Done = True;
         select 
            Board(Traveler.Position.X, Traveler.Position.Y).Occupy;
            Done := True;
         else
            Traveler.Position := (
               X => Integer( Float'Floor( Float( Board_Width )  * Random(G)  ) ),
               Y => Integer( Float'Floor( Float( Board_Height ) * Random(G) ) )          
            );
            --Put_Line("new start");
         end select;
      end loop;

      Store_Trace; -- store starting position
      -- Number of steps to be made by the traveler  
      Nr_of_Steps := Min_Steps + Integer( Float(Max_Steps - Min_Steps) * Random(G));
      -- Time_Stamp of initialization
      Time_Stamp := To_Duration ( Clock - Start_Time ); -- reads global clock
    end Init;
    
    -- wait for initialisations of the remaining tasks:
    accept Start do
      null;
    end Start;

   for Step in 0 .. Nr_of_Steps loop

      delay Min_Delay + (Max_Delay - Min_Delay) * Duration(Random(G));
      Time_Stamp := To_Duration(Clock - Start_Time);

      declare
         Success : Boolean := Make_Step;
      begin
         if not Success then
            Traveler.Symbol := Character'Val(Character'Pos(Traveler.Symbol) + 32); 
            --Put_Line("deadlock " & Integer'Image( Traveler.Id ));
            Store_Trace;
            exit;
         end if;
      end;
   end loop;

   Printer.Report(Traces);

  end Traveler_Task_Type;

  type Wildcard_Traveler_Type is record
    Id: Integer;
    Symbol: Character;
    Position: Position_Type; 
    Lifespan : Duration;
    Start_Time : Time := Clock;
    Traces: Traces_Sequence_Type; 
    Finished : Boolean := False;
  end record;

  type Wildcard_Access is access all Wildcard_Traveler_Type;

  task type Wildcard_Manager is
    entry Evict(Id : Integer);
    entry Confirm;
  end Wildcard_Manager;

  task body Wildcard_Manager is
    G : Generator;
    --  X : Integer;
    --  Y : Integer;
    Digit : Character;
    package Wildcard_Vector is new Ada.Containers.Vectors (Index_Type   => Natural, Element_Type => Wildcard_Access);
    use Wildcard_Vector;
    V : Vector;
    --  Wildcard : Wildcard_Traveler_Type;
    C : Cursor;
    Id_Counter : Integer := Nr_Of_Travelers;

    Last_Time : Time := Clock;

    procedure Store_Trace(Wildcard : Wildcard_Access) is
    begin  
      Wildcard.Traces.Last := Wildcard.Traces.Last + 1;
      Wildcard.Traces.Trace_Array( Wildcard.Traces.Last ) := ( 
          Time_Stamp => To_Duration (Clock - Start_Time),
          Id => Wildcard.Id,
          Position => Wildcard.Position,
          Symbol => Wildcard.Symbol
        );
    end Store_Trace;


  begin
    Reset (G);
    loop
          -- Operate on wildcards after X seconds
      for E of V loop
         if not E.Finished and then To_Duration(Clock - E.Start_Time) >= 0.2 then
            -- Example operation: change symbol or mark as operated
            --  E.Position.X := Board_Width;
            --  E.Position.Y := Board_Height;
            E.Symbol := '?';
            E.Finished := True;
            Store_Trace (E);
            Printer.Report(E.Traces);
            --  Put_Line("Operated on wildcard " & Integer'Image(E.Id));
         end if;
      end loop;

      -- Spawn new wildcard every Y seconds
      if To_Duration(Clock - Last_Time) >= 0.5 then
         declare
            Wildcard : constant Wildcard_Access := new Wildcard_Traveler_Type;
            X      : Integer := Integer(Float'Floor(Float(Board_Width) * Random(G)));
            Y      : Integer := Integer(Float'Floor(Float(Board_Height) * Random(G)));
            Digit  : Character := Character'Val(Character'Pos('0') + Integer(Float'Floor(10.0 * Random(G))));
         begin
            Wildcard.Id := Id_Counter;
            Id_Counter := Id_Counter + 1;
            Wildcard.Start_Time := Clock;
            Wildcard.Lifespan := 0.5;  -- or set dynamically
            Wildcard.Position.X := X;
            Wildcard.Position.Y := Y;
            Wildcard.Symbol := Digit;
            Wildcard.Finished := False;

            --  Put_Line (Duration'Image(To_Duration(Wildcard.Start_Time - Start_Time)));
            Store_Trace (Wildcard);
            V.Append(Wildcard);
            --  Put_Line("Spawned new wildcard " & Integer'Image(Wildcard.Id));
         end;
         Last_Time := Clock;
      end if;
  
      
      select
        accept Evict(Id : Integer) do
          for E of V loop
            if E.Id = Id then
                --  Old_Position : Position_Type := Traveler.Position;
                --     New_Position : Position_Type := Old_Position;
                --     N : Integer := Integer( Float'Floor(4.0 * Random(G)) );
                --  begin
                --     case N is
                --        when 0 => New_Position.Y := (Old_Position.Y + Board_Height - 1) mod Board_Height;
                --        when 1 => New_Position.Y := (Old_Position.Y + 1) mod Board_Height;
                --        when 2 => New_Position.X := (Old_Position.X + Board_Width - 1) mod Board_Width;
                --        when 3 => New_Position.X := (Old_Position.X + 1) mod Board_Width;
                --        when others =>  return False;
                --     end case;

              declare
                Old_Position : Position_Type := E.Position;
                New_Position : Position_Type := Old_Position;
                N : Integer := Integer( Float'Floor(4.0 * Random(G)) );
              begin
                case N is
                  when 0 => New_Position.Y := (Old_Position.Y + Board_Height - 1) mod Board_Height;
                  when 1 => New_Position.Y := (Old_Position.Y + 1) mod Board_Height;
                  when 2 => New_Position.X := (Old_Position.X + Board_Width - 1) mod Board_Width;
                  when 3 => New_Position.X := (Old_Position.X + 1) mod Board_Width;
                  when others =>  null;
                end case;
                
                Board(New_Position.X, New_Position.Y).Add_Wildcard (E.Id);
                E.Position := New_Position;
                Store_Trace (E);
              end;
              --  TODO move to a neigboring square
              requeue Board(E.Position.X, E.Position.Y).all.Remove_Wildcard;
            end if;
          end loop;
        end Evict;
      or
        accept Confirm;
      or
        delay 0.0;
      end select;

    end loop;
  end Wildcard_Manager;


  Travel_Tasks: array (0 .. Nr_Of_Travelers-1) of Traveler_Task_Type; -- for tests
  Symbol : Character := 'A';

  Wildcard_Manager_Task : Wildcard_Manager;

  task body Board_Cell_Task_Type is
      --  Occupant : Traveler_Type;
      Occupied : Boolean := False;
      Running  : Boolean := True;

      Has_Wildcard : Boolean := False;
      Wildcard_Trace : Traces_Sequence_Type;
      Wildcard_Id : Integer := -1;

  begin
      loop
        exit when not Running;
        select
           when not Occupied =>
              accept Occupy do
                if Has_Wildcard then
                  Wildcard_Manager_Task.Evict(Wildcard_Id);
                  
                end if;
                 Occupied := True;
              end Occupy;
        or
           when Occupied =>
              accept Leave do
                 Occupied := False;
              end Leave;
        or  
              accept Stop do
                  Running := False;
              end Stop;
        or
          when not Occupied and not Has_Wildcard =>
            accept Add_Wildcard (Id : Integer) do
                Has_Wildcard := True;
                Wildcard_Id := Id;
            end Add_Wildcard;
        or
          when Has_Wildcard =>
            accept Remove_Wildcard do
              Has_Wildcard := False;
              Wildcard_Id := -1;
            end Remove_Wildcard;
        end select;
     end loop;
  end Board_Cell_Task_Type;


begin 
   for X in 0 .. Board_Width - 1 loop
    for Y in 0 .. Board_Height - 1 loop
        Board(X, Y) := new Board_Cell_Task_Type;
    end loop;
  end loop;
  
  -- Prit the line with the parameters needed for display script:
  Put_Line(
      "-1 "&
      Integer'Image( Nr_Of_Travelers ) &" "&
      Integer'Image( Board_Width ) &" "&
      Integer'Image( Board_Height )      
    );

  -- init tarvelers tasks
  for I in Travel_Tasks'Range loop
    Travel_Tasks(I).Init( I, Seeds(I+1), Symbol );   -- `Seeds(I+1)` is ugly :-(
    Symbol := Character'Succ( Symbol );
  end loop;

  -- start tarvelers tasks
  for I in Travel_Tasks'Range loop
      Travel_Tasks(I).Start;
  end loop;

  --  for X in 0 .. Board_Width - 1 loop
  --   for Y in 0 .. Board_Height - 1 loop
  --      Board(X, Y).Stop;
  --   end loop;
  --  end loop;


end Travelers;

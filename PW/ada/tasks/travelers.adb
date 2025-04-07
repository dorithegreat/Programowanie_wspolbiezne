with Ada.Text_IO; use Ada.Text_IO;
with Ada.Numerics.Float_Random; use Ada.Numerics.Float_Random;
with Random_Seeds; use Random_Seeds;
with Ada.Real_Time; use Ada.Real_Time;

procedure  Travelers is


-- Travelers moving on the board

  Nr_Of_Travelers : constant Integer := 15;

  Min_Steps : constant Integer := 10 ;
  Max_Steps : constant Integer := 100 ;

  Min_Delay : constant Duration := 0.01;
  Max_Delay : constant Duration := 0.05;

-- 2D Board with torus topology

  Board_Width  : constant Integer := 7;
  Board_Height : constant Integer := 7;

-- Timing

  Start_Time : Time := Clock;  -- global startnig time

-- Random seeds for the tasks' random number generators
 
  Seeds : Seed_Array_Type(1..Nr_Of_Travelers) := Make_Seeds(Nr_Of_Travelers);

-- Types, procedures and functions

  -- Postitions on the board
  type Position_Type is record	
    X: Integer range 0 .. Board_Width - 1; 
    Y: Integer range 0 .. Board_Height - 1; 
  end record;

    protected type Board_Cell is 
        entry Enter;
        entry Leave;
    private
          Occupied : Boolean := False;
    end Board_Cell;

    protected body Board_Cell is
      entry Enter when not Occupied is
      begin
        Occupied := True;
      end Enter;

      entry Leave when Occupied is
      begin
        Occupied := False;
      end Leave;
    end Board_Cell;

   type Board_Type is array (0 .. Board_Width - 1, 0 .. Board_Height - 1) of Board_Cell;

   Board : Board_Type;

  -- elementary steps
  procedure Move_Down( Position: in out Position_Type ) is
  begin
    Position.Y := ( Position.Y + 1 ) mod Board_Height;
  end Move_Down;

  procedure Move_Up( Position: in out Position_Type ) is
  begin
    Position.Y := ( Position.Y + Board_Height - 1 ) mod Board_Height;
  end Move_Up;

  procedure Move_Right( Position: in out Position_Type ) is
  begin
    Position.X := ( Position.X + 1 ) mod Board_Width;
  end Move_Right;

  procedure Move_Left( Position: in out Position_Type ) is
  begin
    Position.X := ( Position.X + Board_Width - 1 ) mod Board_Width;
  end Move_Left;

  -- traces of travelers
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
    for I in 1 .. Nr_Of_Travelers loop -- range for TESTS !!!
        accept Report( Traces : Traces_Sequence_Type ) do
          Print_Traces( Traces );
        end Report;
      end loop;
  end Printer;


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
    Done : Boolean := False;
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
    
    procedure Make_Step is
      N : Integer; 
      New_Position : Position_Type;
    begin
      if Traveler.Symbol in 'a' .. 'z' then
         Done := True;
         return;
      end if;

      N := Integer( Float'Floor(4.0 * Random(G)) );    
      New_Position := Traveler.Position;
      case N is
        when 0 =>
         --   Move_Up( Traveler.Position );
         New_Position.Y := (Traveler.Position.Y + Board_Height - 1) mod Board_Height;
        when 1 =>
         --   Move_Down( Traveler.Position );
         New_Position.Y := (Traveler.Position.Y + 1) mod Board_Height;
        when 2 =>
         --   Move_Left( Traveler.Position );
         New_Position.X := (Traveler.Position.X + Board_Width - 1) mod Board_Width;
        when 3 =>
         --   Move_Right( Traveler.Position );
         New_Position.X := (Traveler.Position.X + 1) mod Board_Width;
        when others =>
          Put_Line( " ?????????????? " & Integer'Image( N ) );
        end case;

        select
          Board (New_Position.X, New_Position.Y).Enter;
          --  Put_Line (Standard_Error, "Traveler " 
          --  & Traveler.Symbol 
          --  & " leaving " 
          --  & Integer'Image(Traveler.Position.X)
          --  & ", " 
          --  & Integer'Image(Traveler.Position.Y)
          --  & " and entering "
          --  & Integer'Image(New_Position.X)
          --  & ", "
          --  & Integer'Image(New_Position.Y));
          Board (Traveler.Position.X, Traveler.Position.Y).Leave;
          Traveler.Position := New_Position;
        else 
          delay Max_Delay;
          Traveler.Symbol := Character'Val (Character'Pos (Traveler.Symbol) + 32);

        end select;


    end Make_Step;

  begin
    accept Init(Id: Integer; Seed: Integer; Symbol: Character) do
      Reset(G, Seed); 
      Traveler.Id := Id;
      Traveler.Symbol := Symbol;
      -- Random initial position:
      loop
        Traveler.Position := (
            X => Integer( Float'Floor( Float( Board_Width )  * Random(G)  ) ),
            Y => Integer( Float'Floor( Float( Board_Height ) * Random(G) ) )          
        );
        select
            Board(Traveler.Position.X, Traveler.Position.Y).Enter;
            exit; -- got a free cell
        else
            delay 0.0; -- try again immediately
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
      delay Min_Delay+(Max_Delay-Min_Delay)*Duration(Random(G));
      -- do action ...
      Make_Step;
      Store_Trace;
      Time_Stamp := To_Duration ( Clock - Start_Time ); -- reads global clock
    end loop;
    Printer.Report( Traces );
  end Traveler_Task_Type;


-- local for main task

  Travel_Tasks: array (0 .. Nr_Of_Travelers-1) of Traveler_Task_Type; -- for tests
  Symbol : Character := 'A';
begin 
  
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

end Travelers;


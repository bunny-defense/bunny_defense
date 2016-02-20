open Printf

(*
let file = "example.dat"
let message = "Hello!"
  
let () =
  (* Write message to file *)
  let oc = open_out file in    (* create or truncate file, return channel *)
  fprintf oc "%s\n" message;   (* write something *)   
  close_out oc;                (* flush and close the channel *)
*)

let frs (x,y,z,t) = x;;
let scd (x,y,z,t) = y;;
let trd (x,y,z,t) = z;;
let frt (x,y,z,t) = t;;


Random.init (int_of_float(Unix.gettimeofday ()));;
  
let random_int n = Random.int n;;
  
let n_wave = int_of_string(Sys.argv.(1));;

(* The following functions are to be targeted for game balancing matters ; they define crucial values regarding difficulty *)
let difficulty = 20 + n_wave*n_wave;;
let spawn_time = 0.2 +. 0.8/.((float_of_int n_wave)**0.7);; (* 1 sec at wave 1, decreases to 0.2 sec as game goes *)

  
let bunnies = [|"Bunny",1,200,1 ; "HeavyBunny",3,50,1 ; "Hare",1,35,3|];;
let bosses = [|"Otter",15,10,10|];;
(* bunnies and bosses : list of (bunny type, difficulty points, inverse rarity, first possible wave of appearance) *)
let sum_rarity_bunn =
  let res = ref 0 in
  for i=0 to Array.length(bunnies)-1 do
    res := (!res) + (trd bunnies.(i))
  done;
  !res;;
  
let sum_rarity_boss =
  let res = ref 0 in
  for i=0 to Array.length(bosses)-1 do
    res := (!res) + (trd bosses.(i))
  done;
  !res;;
  
let file_name = "../wave" ^ (string_of_int n_wave) ^ ".csv";;
let oc = open_out file_name;;

let chose_bunny i =
  (* i is a random integer between 0 and sum_rarity_bunn-1, choses a random bunny based on rarity *)
  let j = ref (i - (trd bunnies.(0)))
  and bunny_index = ref 0 in
  while (!j >= 0) && (!bunny_index < Array.length(bunnies)) do
    incr bunny_index;
    j:= (!j) - (trd bunnies.(!bunny_index))
  done;
  !bunny_index;;

 let chose_boss i =
  (* i is a random integer between 0 and sum_rarity_boss-1, choses a random boss based on rarity *)
  let j = ref (i - (trd bosses.(0)))
  and boss_index = ref 0 in
  while (!j >= 0) && (!boss_index < Array.length(bosses)) do
    incr boss_index;
    j:= (!j) - (trd bosses.(!boss_index))
  done;
  !boss_index;;

let rec wave n t=
  (* Prints a NON BOSS wave corresponding to the difficulty n in file wave_{n_wave}, starting at time t *)
  match n with
  |n when n <= 0
      -> ()
  |n  ->  let bunny = bunnies.(chose_bunny (random_int sum_rarity_bunn)) in
	  let diff_decr = ref 0 in 
	  if n_wave >= frt bunny then
	    begin
	      let diff = scd bunny in
	      fprintf oc "%f, %s\n" t (frs bunny);
	      diff_decr := diff
	    end;
	  wave (n-(!diff_decr)) (t+.spawn_time);;
  
  
let print_wave n t=
  let r = random_int 100 in
  if (n_wave >= 10) && (r < 6) then
    (* BOSS TIME, B*TCHES *)
    begin
      let boss=bosses.(chose_boss (random_int sum_rarity_boss)) in
      fprintf oc "%f, %s\n" t (frs boss)
    end
  else
    (* No boss for now... *)
    wave n t;;
  
  
print_wave difficulty 0.;;
    

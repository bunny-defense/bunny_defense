open Printf;;
  
       
let frs (x,y,z,t) = x;;
let scd (x,y,z,t) = y;;
let trd (x,y,z,t) = z;;
let frt (x,y,z,t) = t;;


Random.init (int_of_float(Unix.gettimeofday ()));;
  
let random_int n = Random.int n;;
  
let n_wave = int_of_string(Sys.argv.(1));;

(* The following functions are to be targeted for game balancing matters ; they define crucial values regarding difficulty *)
let difficulty = 20 + int_of_float(((float_of_int n_wave)/.1.5) **4.);;
let spawn_time = 0.001 +. 2./.((float_of_int n_wave)**1.35);; (* 1 sec at wave 1, decreases to 0.1 sec as game goes *)

let atan_variation init_val final_val inflex_point = (* int -> float *)
  (* Returns a function going from init_val to final_val with atangent variations, with an inflexion point at inflex_point *)
  if init_val <= final_val
  then
    function x -> (1.57 +. atan(float_of_int(x) -. inflex_point))*.((final_val -. init_val)/.(3.1416)) +. init_val
  else
    function x -> (1.57 +. atan(inflex_point -. float_of_int(x)))*.((init_val -. final_val)/.(3.1416)) +. final_val;;

let bunnies_alone =
  [|
    "Bunny", 1, 1, atan_variation 200. 10. 20. ;
    "HeavyBunny", 3, 1, atan_variation 50. 175. 10. ;
    "Hare", 1, 3, atan_variation 25. 40. 10. ;
    "BadassBunny", 5, 4, atan_variation 0. 200. 15. ;
    "SpecOpBunny", 5, 7, atan_variation 0. 40. 20. ;
    "FlyingSquirrel", 4, 3, atan_variation 10. 15. 10.
   |];;
let bosses_alone = [|"Otter",1500,10,atan_variation 1. 10. 20.|];;
(* bunnies and bosses : list of (bunny type, difficulty points, first possible wave of appearance, inverse rarity as a function of n_wave) *)
let bunnies = Array.concat [bunnies_alone; bosses_alone];;

let min_difficulty =
  (* The lowest difficulty of all bunnies *)
  let res = ref (scd bunnies.(0)) in
  for i=0 to Array.length(bunnies)-1 do
    if (scd bunnies.(i)) < (!res)
    then res := (scd bunnies.(i))
  done;
  !res;;
  
let sum_rarity =
  let res = ref 0. in
  for i=0 to Array.length(bunnies)-1 do
    res := (!res) +. ((frt bunnies.(i)) n_wave)
  done;
  !res;;

  
let file_name = "../wave" ^ (string_of_int n_wave) ^ ".csv";;
let oc = open_out file_name;;

let chose_bunny i =
  (* i is a random integer between 0 and sum_rarity_bunn-1, choses a random bunny based on rarity *)
  let j = ref (float_of_int(i) -. ((frt bunnies.(0)) n_wave))
  and bunny_index = ref 0 in
  while (!j >= 0.) && (!bunny_index < Array.length(bunnies)) do
    incr bunny_index;
    j:= (!j) -. ((frt bunnies.(!bunny_index)) n_wave)
  done;
  !bunny_index;;

let rec wave n t=
  (* Prints a wave corresponding to the difficulty n in file wave_{n_wave}, starting at time t *)
  match n with
  |n when n <= 0
      -> ()
  |n  ->  let bunny = bunnies.(chose_bunny (random_int (int_of_float sum_rarity))) in
	  (* Note that we need a random integer below rarity which is an integer...
             Not a big deal, but worth noting the small approximation we're making *)
	  let diff_decr = ref 0
	  and time_elapsed = ref spawn_time in
	  if n_wave >= trd bunny then
	    begin
	      let diff = scd bunny in
	      if n-diff >= 0
	      then
		begin
		  fprintf oc "%f, %s\n" t (frs bunny);
		  diff_decr := diff;
		end
	      else
		begin
		  diff_decr := 0; (* The bunny selected cannot spawn, let's try again with another bunny.
                                    (Difficulty level unchanged since nothing spawned) *)
		  time_elapsed := 0.
		end;
	    end;
	  if (n-(!diff_decr)) >= 0
	  then wave (n-(!diff_decr)) (t+.(!time_elapsed))
	  else if n >= min_difficulty
	  then wave n t (* Even if a bunny can't spawn now, one can possibly spawn, so let us try again *)
	  else ()(* Yeah nothing's gonna spawn bro *);;
  
  
wave difficulty 0.;;
    

open Printf;;
  
       
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

let atan_variation init_val final_val inflex_point = (* int -> float *)
  (* Returns a function going from init_val to final_val with atangent variations, with an inflexion point at inflex_point *)
  if init_val <= final_val
  then
    function x -> (1.57 +. atan(float_of_int(x) -. inflex_point))*.((final_val -. init_val)/.(3.1416)) +. init_val
  else
    function x -> (1.57 +. atan(inflex_point -. float_of_int(x)))*.((init_val -. final_val)/.(3.1416)) +. final_val;;

let bunnies = [|"Bunny",1,1,atan_variation 200. 10. 20. ; "HeavyBunny",3,1,atan_variation 50. 100. 15. ; "Hare",1,3,atan_variation 25. 40. 10.|];;
let bosses = [|"Otter",15,10,atan_variation 10. 10. 1.|];;
(* bunnies and bosses : list of (bunny type, difficulty points, first possible wave of appearance, inverse rarity as a function of n_wave) *)
let sum_rarity_bunn =
  let res = ref 0. in
  for i=0 to Array.length(bunnies)-1 do
    res := (!res) +. ((frt bunnies.(i)) n_wave)
  done;
  !res;;
  
let sum_rarity_boss =
  let res = ref 0. in
  for i=0 to Array.length(bosses)-1 do
    res := (!res) +. ((frt bosses.(i)) n_wave)
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

 let chose_boss i =
  (* i is a random integer between 0 and sum_rarity_boss-1, choses a random boss based on rarity *)
  let j = ref (float_of_int(i) -. ((frt bosses.(0)) n_wave))
  and boss_index = ref 0 in
  while (!j >= 0.) && (!boss_index < Array.length(bosses)) do
    incr boss_index;
    j:= (!j) -. ((frt bosses.(!boss_index)) n_wave)
  done;
  !boss_index;;

let rec wave n t=
  (* Prints a NON BOSS wave corresponding to the difficulty n in file wave_{n_wave}, starting at time t *)
  match n with
  |n when n <= 0
      -> ()
  |n  ->  let bunny = bunnies.(chose_bunny (random_int (int_of_float sum_rarity_bunn))) in
	  (* Note that we need a random integer below rarity which is an integer...
             Not a big deal, but worth noting the small approximation we're making *)
	  let diff_decr = ref 0 in 
	  if n_wave >= trd bunny then
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
      let boss=bosses.(chose_boss (random_int (int_of_float sum_rarity_boss))) in
      (* Same as before, we're making a small approximation *)
      fprintf oc "%f, %s\n" t (frs boss)
    end
  else
    (* No boss for now... *)
    wave n t;;
  
  
print_wave difficulty 0.;;
    

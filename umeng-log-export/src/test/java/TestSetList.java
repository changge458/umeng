public class TestSetList {
    public static void main(String[] args) {
        SetList<Integer> list = new SetList<>();

        list.add(111);
        list.add(222);
        list.add(333);
        list.add(111);
        list.add(555);
//        for(int i = 0; i < list.size(); i++){
//            System.out.println(list.get(i));
//
//        }
        SetList<Integer> clone = (SetList) list.clone();
//        for(Integer j : clone){
//            System.out.println(j);
//        }


//        for(int i = 0; i< list.size(); i++ ){
//            for(int j = i + 1; j < list.size(); j++){
//                System.out.println(list.get(i) + "," + list.get(j));
//            }
//
//        }

    }
}

class test{
	
	public static void main(String[] args){
		BSP test = new BSP("./../../resources/Scenes/first/octangle.txt", BSP.RANDOM);
		printBSP(test);

		
	}

	public static void printBSP(BSP bsp){
		printrecBSP(bsp.getRoot());
	}

	private static void printrecBSP(Node root){
		System.out.println(root.toString());
		if(root.getLeft()!=null)
			printrecBSP(root.getLeft());
		if(root.getRight()!=null)
			printrecBSP(root.getRight());
	}
}
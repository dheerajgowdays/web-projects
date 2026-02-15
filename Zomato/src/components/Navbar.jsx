function Navbar(){
    return(
        <div className="bg-white shadow-md px-8 py-4 flex justify-between items-center">
            <h1 className="text-2xl font-bold text-red-500"> Zomato</h1>
            <div className="space-x-6 font-medium">
            <button>Home</button>
            <button>Cart  🛒</button>
            </div>
        </div>
    )
}
export default Navbar
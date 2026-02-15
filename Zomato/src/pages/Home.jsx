import Navbar from "../components/Navbar";
import RestaurantCard from "../components/RestaurantCard";
import restaurants from "../data/restaruants";

function Home() {
    return(
        <div className="bg-gray-100 min-h-screen">
            <Navbar/>
            <div className="p-8 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8">
                {restaurants.map((r) => (
                    <RestaurantCard key={r.id} restaurant = {r}/>
                ))}
            </div>
        </div>
    )
}
export default Home
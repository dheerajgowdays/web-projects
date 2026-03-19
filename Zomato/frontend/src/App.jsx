import { Routes , Route , Navigate  } from "react-router-dom"
import  { useAuthStore } from './store/authStore'

function HomePage(){
   return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-primary-500">🍕 Food </h1>
      <p className="mt-2 text-gray-600"> Phase 1 complete — infrastructure running!</p>
    </div>
   );
}

function LoginPage(){
  return <div className="p-8">Login Page</div>;
}

function RegisterPage(){
  return <div className="p-8">Register Page</div>;
}

function RestaurantsPage(){
  return <div className="p-8">Restaurent Page</div>;
}

function NotFoundPage(){
  return (<div className="p-8 text-center">
    <h1 className="text-4xl font-bold">404</h1>
  </div>
  );
}

function ProtectedRoute(props){
  const isAuthenticated = useAuthStore(function(s){
    return s.isAuthenticated;
  });

  if(isAuthenticated){
    return props.childern;
  }else{
    return <Navigate to="/login" replace />
  }
}

function PublicRoute(props){
  const isAuthenticated = useAuthStore(function(s){
    return s.isAuthenticated;
  });

  if(isAuthenticated){
    return <Navigate to="/" replace />;
  } else{
    return props.childern;
  }
}

export default function App() {
  return(
    <Routes>
      <Route path="/" element={<HomePage />}/>
      <Route path="/restaurants" element={ <RestaurantsPage />} />
      <Route path="/login" element={<PublicRoute><LoginPage/></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
      <Route path="/orders" element={<ProtectedRoute><div className="p-8">Orders</div></ProtectedRoute>} />
      <Route path="/profile" element={<ProtectedRoute><div className="p-8">Profile</div></ProtectedRoute>} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../services/api";
import type { Course } from "../types";
import { Search, DollarSign, Users, Clock, ShoppingCart } from "lucide-react";

export function Courses() {
    const [courses, setCourses] = useState<Course[]>([]);
    const [filteredCourses, setFilteredCourses] = useState<Course[]>([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isLoading,setIsLoading] = useState(true);
    const [purchasing, setPurchasing] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchCourses();
    }, []);

    useEffect(() => {
        const filtered = courses.filter(course =>
            course.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            course.description.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredCourses(filtered);
    }, [searchTerm, courses]);

    const fetchCourses = async () => {
        try {
           const response = await api.get('/courses');
           setCourses(response.data);
           setFilteredCourses(response.data);
        } catch (error) {
            console.error('Failed to fetch courses:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handlePurchase = async (courseId: string) => {
        setPurchasing(courseId);
        try {
            await api.post(`/courses/${courseId}/purchase`);
            alert('Course purchased successfully!');
            navigate('/dashboard');
        } catch (error) {
            console.error('Purchase failed. Please try again later.', error);
        } finally {
            setPurchasing(null);
        }
    };

    if (isLoading) {
        return <div className="loading">Loading courses...</div>;
    }


    return (
        <div className="courses-page">
            <div className="courses-header">
                <div className="container">
                    <h1>Explore Our Courses</h1>
                    <p>Discover the best courses to advance your career</p>
                    <div className="search-bar">
                        <Search size={20} />
                        <input 
                            type="text" 
                            placeholder="Search courses..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                </div>
            </div>

            <div className="container">
                <div className="courses-grid">
                    {filteredCourses.map((course) => (
                        <div key={course.id} className="course-card card">
                            <img src={course.imageUrl} alt={course.title} className="course-image" />
                            <div className="course-content">
                                <h3>{course.title}</h3>
                                <p>{course.description}</p>
                                <div className="course-meta">
                                    <span><Users size={16} /> {course.enrolledStudents}</span>
                                    <span><Clock size={16} /> {course.totalDuration}</span>
                                </div>
                                <div className="course-footer">
                                    <span className="course-price"><DollarSign size={16} /> {course.price}</span>
                                    <button className="btn btn-primary" onClick={() => handlePurchase(course.id)} disabled={purchasing === course.id}>
                                        {purchasing === course.id ? 'Processing..' : <><ShoppingCart size={16} /> Buy Now</>}
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};
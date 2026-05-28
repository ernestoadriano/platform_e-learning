import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { api } from "../services/api";
import type { Course, Certificate } from "../types";
import { BookOpen, Award, ChevronRight } from "lucide-react";

export const Dashboard = () => {
    const { user } = useAuth();
    const [myCourses, setMyCourses] = useState<Course[]>([]);
    const [certificates, setCertificates] = useState<Certificate[]>([]);
    const [progress, setProgress] = useState<any>({});
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            const [coursesRes, certificatesRes] = await Promise.all([
                api.get('/courses/user/purchased'),
                api.get('/certificates/me')
            ]);

            setMyCourses(coursesRes.data);
            setCertificates(certificatesRes.data);
            
            // Buscar progresso para cada curso
            const progressPromises = coursesRes.data.map((course: Course) =>
                api.get(`/courses/${course.id}/progress`)
            );
            const progressResults = await Promise.all(progressPromises);
            const progressMap: Record<string, any> = {};
            progressResults.forEach((res, index) => {
                const courseId = coursesRes.data[index].id;
                const progressData = res.data;
                
                // Log para debug - veja o que está chegando do backend
                console.log(`Progress for course ${courseId}:`, progressData);
                
                progressMap[courseId] = {
                    completedLessons: progressData.completedLessons || 0,
                    totalLessons: progressData.totalLessons || 1,
                    percentage: progressData.percentage || 0
                };
            });
            setProgress(progressMap);
            
        } catch (error) {
            console.error('Failed to fetch dashboard data:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const getProgressPercentage = (courseId: string) => {
        const courseProgress = progress[courseId];
        if (!courseProgress) return 0;
        
        // Garantir que temos números válidos
        const completed = Number(courseProgress.completedLessons) || 0;
        const total = Number(courseProgress.totalLessons) || 1;
        
        if (total === 0) return 0;
        
        const percentage = Math.round((completed / total) * 100);
        return isNaN(percentage) ? 0 : percentage;
    };

    if (isLoading) {
        return <div className="loading">Loading dashboard...</div>;
    }

    return (
        <div className="dashboard">
            <div className="dashboard-header">
                <div className="container">
                    <h1>Welcome back, {user?.name}</h1>
                    <p>Continue your learning journey</p>
                </div>
            </div>

            <div className="container">
                <div className="dashboard-grid">
                    <div className="dashboard-main">
                        <h2>My Courses</h2>
                        <div className="courses-list">
                            {myCourses.length === 0 ? (
                                <div className="card">
                                    <p>You haven't purchased any courses yet.</p>
                                    <button 
                                        className="btn btn-primary"
                                        onClick={() => navigate('/courses')}
                                    >
                                        Browse Courses
                                    </button>
                                </div>
                            ) : (
                                myCourses.map((course) => (
                                    <div key={course.id} className="course-progress-card card">
                                        <div className="course-info">
                                            <h3>{course.title}</h3>
                                            <p>{course.description}</p>
                                            <div className="progress-bar">
                                                <div 
                                                    className="progress-fill"
                                                    style={{ width: `${getProgressPercentage(course.id)}%` }}
                                                />
                                            </div>
                                            <span className="progress-text">{getProgressPercentage(course.id)}% Complete</span>
                                        </div>
                                        <button 
                                            className="btn btn-secondary"
                                            onClick={() => navigate(`/courses/${course.id}`)}
                                        >
                                            Continue <ChevronRight size={16} />
                                        </button>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>

                    <div className="dashboard-sidebar">
                        <div className="certificates-card card">
                            <h3><Award size={20} /> Certificates</h3>
                            {certificates.length > 0 ? (
                                <ul>
                                    {certificates.map((cert) => (
                                        <li key={cert.id}>
                                            <span>{cert.courseTitle}</span>
                                            <small>{new Date(cert.completionDate).toLocaleDateString()}</small>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>No certificates yet. Complete courses to earn them!</p>
                            )}
                        </div>

                        <div className="stats-card card">
                            <h3><BookOpen size={20} /> Learning Stats</h3>
                            <div className="stats">
                                <div className="stat">
                                    <span className="stat-value">{myCourses.length}</span>
                                    <span className="stat-label">Enrolled Courses</span>
                                </div>
                                <div className="stat">
                                    <span className="stat-value">{certificates.length}</span>
                                    <span className="stat-label">Certificates</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
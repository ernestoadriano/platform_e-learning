import logoUrl from '../assets/logo.svg';

interface LogoProps {
    size?: number;
    className: string;
}

export const Logo = ({ size = 32, className = ''}: LogoProps) => {
    return (
        <img 
            src={logoUrl} 
            alt="E-Learning Platform" 
            width={size}
            className={className}
        />
    );
}
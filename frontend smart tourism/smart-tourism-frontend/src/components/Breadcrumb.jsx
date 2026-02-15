import { Link } from "react-router-dom";

const Breadcrumb = ({ items }) => {
  return (
    <nav className="mb-3">
      <ol className="breadcrumb">
        {items.map((item, index) => (
          <li
            key={index}
            className={`breadcrumb-item ${
              item.active ? "active" : ""
            }`}
          >
            {item.active ? (
              item.label
            ) : (
              <Link to={item.path}>{item.label}</Link>
            )}
          </li>
        ))}
      </ol>
    </nav>
  );
};

export default Breadcrumb;

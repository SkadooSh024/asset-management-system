function PageHeader({ title, description, action }) {
  return (
    <div className="page-header">
      <div>
        <p className="page-eyebrow">Hệ Thống Quản Lý Tài Sản</p>
        <h1>{title}</h1>
        {description ? <p className="page-description">{description}</p> : null}
      </div>
      {action ? <div className="page-action">{action}</div> : null}
    </div>
  );
}

export default PageHeader;
